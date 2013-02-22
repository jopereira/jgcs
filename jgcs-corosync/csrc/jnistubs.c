
/*
 * Corosync CPG implementation of JGCS - Group Communication Service.
 * Copyright (C) 2013 Universidade do Minho
 *
 * jop@di.uminho.pt - http://www.di.uminho.pt/~jop
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */

#include "net_sf_jgcs_corosync_jni_ClosedProcessGroup.h"

#include <corosync/corotypes.h>
#include <corosync/cpg.h>

#include <stdlib.h>
#include <string.h>
#include <sys/uio.h>

/*
   We cache IDs. This depends on classes not being reloaded without
   initialization being called again.
*/
static jfieldID fid_CPG_handle;
static jfieldID fid_CPG_callbacks;

static jmethodID mid_CPG_deliver;
static jmethodID mid_CPG_confchg;
static jmethodID mid_CPG_ringchg;

static jmethodID mid_CPG_new_Address;

static void setup(JNIEnv *env) {
	jclass clazz;

	clazz=(*env)->FindClass(env, "net/sf/jgcs/corosync/jni/ClosedProcessGroup");
	fid_CPG_handle=(*env)->GetFieldID(env, clazz, "handle", "J");
	fid_CPG_callbacks=(*env)->GetFieldID(env, clazz, "callbacks", "Lnet/sf/jgcs/corosync/jni/ClosedProcessGroup$Callbacks;");

	clazz=(*env)->FindClass(env, "net/sf/jgcs/corosync/jni/ClosedProcessGroup$Callbacks");
	mid_CPG_deliver=(*env)->GetMethodID(env, clazz, "deliver", "(Ljava/lang/String;II[B)V");
	mid_CPG_confchg=(*env)->GetMethodID(env, clazz, "configurationChange", "(Ljava/lang/String;[Lnet/sf/jgcs/corosync/CPGAddress;[Lnet/sf/jgcs/corosync/CPGAddress;[I[Lnet/sf/jgcs/corosync/CPGAddress;[I)V");
	mid_CPG_ringchg=(*env)->GetMethodID(env, clazz, "ringChange", "(IJ[I)V");

	clazz=(*env)->FindClass(env, "net/sf/jgcs/corosync/CPGAddress");
	mid_CPG_new_Address=(*env)->GetMethodID(env, clazz, "<init>", "(II)V");
}

/*
	Utilities.
 */
struct context {
	JNIEnv *env;
	jobject self;
};

static inline struct context* get_context(cpg_handle_t handle) {
	void *ctx = NULL;
	cpg_context_get(handle, &ctx);
	return (struct context*) ctx;
}

static inline jstring group_to_jstring(JNIEnv *env, const struct cpg_name* group) {
	char buffer[CPG_MAX_NAME_LENGTH+1];
	memcpy(buffer, group->value, group->length);
	buffer[group->length]=0;
	return (*env)->NewStringUTF(env, buffer);
}

static void throw_CorosyncException(JNIEnv *env, cs_error_t error) {
	jclass clazz = (*env)->FindClass(env, "net/sf/jgcs/corosync/jni/CorosyncException");
	const char* message = cs_strerror(error);
	(*env)->ThrowNew(env, clazz, message);
}

static jarray make_member_array(JNIEnv *env, const struct cpg_address *member_list, size_t member_list_entries) {
	int i;
	jclass clazz=(*env)->FindClass(env, "net/sf/jgcs/corosync/CPGAddress");
	jarray members = (*env)->NewObjectArray(env, member_list_entries, clazz, NULL);
	for(i=0;i<member_list_entries;i++)
		(*env)->SetObjectArrayElement(env, members, i, (*env)->NewObject(env, clazz, mid_CPG_new_Address,
			member_list[i].nodeid, member_list[i].pid));
	return members;
}

static jarray make_reason_array(JNIEnv *env, const struct cpg_address *member_list, size_t member_list_entries) {
	int i;
	jarray members = (*env)->NewIntArray(env, member_list_entries);
	for(i=0;i<member_list_entries;i++)
		(*env)->SetIntArrayRegion(env, members, i, 1, (int*)&member_list[i].reason);
	return members;
}

/*
 * Callbacks.
 */
static void cpg_deliver(
        cpg_handle_t handle,
        const struct cpg_name *group_name,
        uint32_t nodeid,
        uint32_t pid,
        void *msg,
        size_t msg_len) {

	struct context* jctx = get_context(handle);
	JNIEnv *env = jctx->env;

	(*env)->PushLocalFrame(env, 10);

	jstring name = group_to_jstring(env, group_name);
	jarray bytes = (*env)->NewByteArray(env, msg_len);
	(*env)->SetByteArrayRegion(env, bytes, 0, msg_len, msg);

	jobject cb = (*env)->GetObjectField(env, jctx->self, fid_CPG_callbacks);
	(*env)->CallVoidMethod(env, cb, mid_CPG_deliver, name, nodeid, pid, bytes);

	(*env)->PopLocalFrame(env, NULL);
}

static void cpg_confchg(
        cpg_handle_t handle,
        const struct cpg_name *group_name,
        const struct cpg_address *member_list, size_t member_list_entries,
        const struct cpg_address *left_list, size_t left_list_entries,
        const struct cpg_address *joined_list, size_t joined_list_entries) {

	struct context* jctx = get_context(handle);
	JNIEnv *env = jctx->env;

	(*env)->PushLocalFrame(env, 10+member_list_entries+left_list_entries+joined_list_entries);

	jobject cb = (*env)->GetObjectField(env, jctx->self, fid_CPG_callbacks);
	(*env)->CallVoidMethod(env, cb, mid_CPG_confchg,
		group_to_jstring(env, group_name),
		make_member_array(env, member_list, member_list_entries), 
		make_member_array(env, left_list, left_list_entries), 
		make_reason_array(env, left_list, left_list_entries), 
		make_member_array(env, joined_list, joined_list_entries),
		make_reason_array(env, joined_list, joined_list_entries));

	(*env)->PopLocalFrame(env, NULL);
}

static void cpg_ringchg(
        cpg_handle_t handle,
		struct cpg_ring_id ring_id,
		uint32_t member_list_entries,
		const uint32_t *member_list) {

	struct context* jctx = get_context(handle);
	JNIEnv *env = jctx->env;

	(*env)->PushLocalFrame(env, 10);

	jarray members = (*env)->NewIntArray(env, member_list_entries);
	(*env)->SetIntArrayRegion(env, members, 0, member_list_entries, (int*)member_list);

	jobject cb = (*env)->GetObjectField(env, jctx->self, fid_CPG_callbacks);
	(*env)->CallVoidMethod(env, cb, mid_CPG_ringchg, ring_id.nodeid, ring_id.seq, members);

	(*env)->PopLocalFrame(env, NULL);
}

JNIEXPORT void JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup__1initialize(JNIEnv *env, jobject self) {
	cpg_handle_t handle;
	cpg_model_v1_data_t data = {
		CPG_MODEL_V1,
		cpg_deliver,
		cpg_confchg,
		cpg_ringchg,
		0
	};

	setup(env);

	struct context* jctx = (struct context*) malloc(sizeof(struct context));
	jctx->self = (*env)->NewGlobalRef(env, self);

	int error = cpg_model_initialize(&handle, CPG_MODEL_V1, (cpg_model_data_t*)&data, jctx);
	if (error != CS_OK) {
		(*env)->DeleteGlobalRef(env, jctx->self);
		free(jctx);

		throw_CorosyncException(env, error);
		return;
	}

	(*env)->SetLongField(env, self, fid_CPG_handle, handle);
}

JNIEXPORT void JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup__1finalize(JNIEnv *env, jobject self) {
	cpg_handle_t handle = (*env)->GetLongField(env, self, fid_CPG_handle);

	struct context* jctx = get_context(handle);
	if (jctx != NULL) {
		(*env)->DeleteGlobalRef(env, jctx->self);
		free(jctx);
	}

	int error = cpg_finalize(handle);
	if (error != CS_OK) {
		throw_CorosyncException(env, error);
		return;
	}
}

JNIEXPORT void JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup_join(JNIEnv *env, jobject self, jstring group) {
	cpg_handle_t handle = (*env)->GetLongField(env, self, fid_CPG_handle);
	struct cpg_name group_name;

	group_name.length = (*env)->GetStringUTFLength(env, group);
	if (group_name.length > sizeof(group_name.value)) {
		throw_CorosyncException(env, CS_ERR_NAME_TOO_LONG);
		return;
	}

	const char *str = (*env)->GetStringUTFChars(env, group, NULL);
	memcpy(group_name.value, str, group_name.length);

	int error = cpg_join(handle, &group_name);
	if (error != CS_OK) {
		throw_CorosyncException(env, error);
		return;
	}
}

JNIEXPORT void JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup_leave(JNIEnv *env, jobject self, jstring group) {
	cpg_handle_t handle = (*env)->GetLongField(env, self, fid_CPG_handle);
	struct cpg_name group_name;

	group_name.length = (*env)->GetStringUTFLength(env, group);
	if (group_name.length > sizeof(group_name.value)) {
		throw_CorosyncException(env, CS_ERR_NAME_TOO_LONG);
		return;
	}

	const char *str = (*env)->GetStringUTFChars(env, group, NULL);
	memcpy(group_name.value, str, group_name.length);

	int error = cpg_leave(handle, &group_name);
	if (error != CS_OK) {
		throw_CorosyncException(env, error);
		return;
	}
}

JNIEXPORT void JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup_dispatch(JNIEnv *env, jobject self, jint mode) {
	cpg_handle_t handle = (*env)->GetLongField(env, self, fid_CPG_handle);

	struct context* jctx = get_context(handle);
	jctx->env = env;
	int error = cpg_dispatch(handle, mode);
	jctx->env = NULL;

	if (error != CS_OK) {
		throw_CorosyncException(env, error);
		return;
	}
}

JNIEXPORT void JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup_multicast(JNIEnv *env, jobject self, jint guarantee, jbyteArray msg) {
	cpg_handle_t handle = (*env)->GetLongField(env, self, fid_CPG_handle);

	struct iovec vec = {
		(*env)->GetByteArrayElements(env, msg, NULL),
		(*env)->GetArrayLength(env, msg)
	};
	int error = cpg_mcast_joined(handle, guarantee, &vec, 1);
	(*env)->ReleaseByteArrayElements(env, msg, vec.iov_base, JNI_ABORT);

	if (error != CS_OK) {
		throw_CorosyncException(env, error);
		return;
	}
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_corosync_jni_ClosedProcessGroup_getLocalNodeId(JNIEnv *env, jobject self) {
	cpg_handle_t handle = (*env)->GetLongField(env, self, fid_CPG_handle);

	unsigned nodeid;
	int error = cpg_local_get(handle, &nodeid);
	if (error != CS_OK) {
		throw_CorosyncException(env, error);
		return 0;
	}

	return nodeid;
}

