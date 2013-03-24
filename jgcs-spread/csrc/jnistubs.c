
/*
 * Spread implementation of JGCS - Group Communication Service.
 * Copyright (C) 2004,2006 Jose' Orlando Pereira, Universidade do Minho
 *
 * jop@di.uminho.pt - http://gsd.di.uminho.pt/~jop
 * jgcs@lasige.di.fc.ul.pt
 *
 * Departamento de Informatica, Universidade do Minho
 * Campus de Gualtar, 4710-057 Braga, Portugal
 *
 * See COPYING for licensing details.
 */

#include "net_sf_jgcs_spread_jni_SpMailbox.h"
#include "net_sf_jgcs_spread_jni_FlMailbox.h"

#include <sp.h>
#include <fl.h>

#include <string.h>

/*
   We cache IDs. This depends on classes not being reloaded without
   initialization being called again. As Mailbox references all other
   classes and calls C_init when loaded, we should be safe.
*/
static jfieldID fid_Mailbox_mailbox;
static jfieldID fid_Mailbox_private_group;

static jfieldID fid_Envelope_mess_type;
static jfieldID fid_Envelope_service_type;
static jfieldID fid_Envelope_group_name;
static jfieldID fid_Envelope_recvr_names;

static jfieldID fid_Envelope_max_groups;
static jfieldID fid_Envelope_endian_mismatch;
static jfieldID fid_Envelope_sender;

static jfieldID fid_View_group_id;
static jfieldID fid_View_vs_set;

static jmethodID mid_ByteBuffer_isDirect;
static jmethodID mid_ByteBuffer_array;
static jmethodID mid_ByteBuffer_arrayOffset;
static jmethodID mid_ByteBuffer_get_position;
static jmethodID mid_ByteBuffer_get_position;
static jmethodID mid_ByteBuffer_set_position;
static jmethodID mid_ByteBuffer_get_limit;

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_Mailbox_C_1init
  (JNIEnv *env, jclass mb_clazz) {
	jclass clazz;

	clazz=(*env)->FindClass(env, "net/sf/jgcs/spread/jni/Mailbox");
	fid_Mailbox_mailbox=(*env)->GetFieldID(env, clazz, "mailbox", "I");
	fid_Mailbox_private_group=(*env)->GetFieldID(env, clazz, "private_group", "Ljava/lang/String;");

	clazz=(*env)->FindClass(env, "net/sf/jgcs/spread/jni/Mailbox$MulticastArgs");
	fid_Envelope_mess_type=(*env)->GetFieldID(env, clazz, "mess_type", "S");
	fid_Envelope_service_type=(*env)->GetFieldID(env, clazz, "service_type", "I");
	fid_Envelope_recvr_names=(*env)->GetFieldID(env, clazz, "groups", "[Ljava/lang/String;");

	clazz=(*env)->FindClass(env, "net/sf/jgcs/spread/jni/Mailbox$ReceiveArgs");
	fid_Envelope_endian_mismatch=(*env)->GetFieldID(env, clazz, "endian_mismatch", "I");
	fid_Envelope_sender=(*env)->GetFieldID(env, clazz, "sender", "Ljava/lang/String;");
	fid_Envelope_group_name=(*env)->GetFieldID(env, clazz, "group_name", "Ljava/lang/String;");
	fid_Envelope_max_groups=(*env)->GetFieldID(env, clazz, "max_groups", "I");

	clazz=(*env)->FindClass(env, "net/sf/jgcs/spread/jni/Mailbox$ViewInfo");
	fid_View_vs_set=(*env)->GetFieldID(env, clazz, "vs_set", "[Ljava/lang/String;");
	fid_View_group_id=(*env)->GetFieldID(env, clazz, "group_id", "[I");

	clazz=(*env)->FindClass(env, "java/nio/ByteBuffer");
	mid_ByteBuffer_isDirect=(*env)->GetMethodID(env, clazz, "isDirect", "()Z");
	mid_ByteBuffer_array=(*env)->GetMethodID(env, clazz, "array", "()[B");
	mid_ByteBuffer_arrayOffset=(*env)->GetMethodID(env, clazz, "arrayOffset", "()I");
	mid_ByteBuffer_get_position=(*env)->GetMethodID(env, clazz, "position", "()I");
	mid_ByteBuffer_set_position=(*env)->GetMethodID(env, clazz, "position", "(I)Ljava/nio/Buffer;");
	mid_ByteBuffer_get_limit=(*env)->GetMethodID(env, clazz, "limit", "()I");

	return 0;
}

/* Spread */

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1connect
  (JNIEnv *env, jobject self, jstring spread_name, jstring private_name,
   jboolean priority, jboolean group_membership) {
	const char *spread, *private=NULL;
	int mbox, ret;
	jstring gstr;
	char gid[MAX_GROUP_NAME];

	spread=(*env)->GetStringUTFChars(env, spread_name, NULL);
	if (private_name!=NULL)
		private=(*env)->GetStringUTFChars(env, private_name, NULL);

	ret=SP_connect(spread, private, priority, group_membership, &mbox, gid);

	(*env)->ReleaseStringUTFChars(env, spread_name, spread);
	if (private!=NULL)
		(*env)->ReleaseStringUTFChars(env, private_name, private);

	(*env)->SetIntField(env, self, fid_Mailbox_mailbox, mbox);

	if (ret>0) {
		gstr=(*env)->NewStringUTF(env, gid);
		(*env)->SetObjectField(env, self, fid_Mailbox_private_group, gstr);
	}

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1disconnect
  (JNIEnv *env, jobject self) {
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	ret=SP_disconnect(mbox);

	(*env)->SetIntField(env, self, fid_Mailbox_mailbox, -1);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1join
  (JNIEnv *env, jobject self, jstring group) {
	const char* grp;
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);
	grp=(*env)->GetStringUTFChars(env, group, NULL);

	ret=SP_join(mbox, grp);

	(*env)->ReleaseStringUTFChars(env, group, grp);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1leave
  (JNIEnv *env, jobject self, jstring group) {
	const char* grp;
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);
	grp=(*env)->GetStringUTFChars(env, group, NULL);

	ret=SP_leave(mbox, grp);

	(*env)->ReleaseStringUTFChars(env, group, grp);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1multicast
  (JNIEnv *env, jobject self, jobject info, jobject mess) {
	int i;
	const char* grp;
	void* buf;
	jstring group;
	jobject grouparr, marray;
	int mbox, mess_type, service_type, ret, pos, len;
	int num_groups;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	mess_type=(*env)->GetIntField(env, info, fid_Envelope_mess_type);
	service_type=(*env)->GetIntField(env, info, fid_Envelope_service_type);
	grouparr=(*env)->GetObjectField(env, info, fid_Envelope_recvr_names);
	if (grouparr==NULL)
		return ILLEGAL_GROUP;
	num_groups=(*env)->GetArrayLength(env, grouparr);

	{
		/* We use a nested block to allocate an array for group names. */
		char groups[num_groups][MAX_GROUP_NAME];
		for(i=0;i<num_groups;i++) {
			jobject grpelem=(*env)->GetObjectArrayElement(env, grouparr, i);
			if (grpelem==NULL)
				return ILLEGAL_GROUP;
			grp=(*env)->GetStringUTFChars(env, grpelem, NULL);
			strcpy(groups[i], grp);
			(*env)->ReleaseStringUTFChars(env, group, grp);
		}

		ret=SP_multigroup_multicast(mbox, service_type, num_groups, (const char(*)[MAX_GROUP_NAME])groups, mess_type, len, buf+pos);
	}

	if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		(*env)->ReleaseByteArrayElements(env, marray, buf, JNI_ABORT);

	if (ret<0)
		return ret;

	(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_set_position, pos+ret);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1receive
  (JNIEnv *env, jobject self, jobject info, jobject mess) {
	const char* grp;
	void* buf;
	int mbox, ret, len, pos, i;
	jobject marray;
	jobjectArray grparr;

	int service_type;
	char sender[MAX_GROUP_NAME];
	int max_groups, num_groups;
	int endian_mismatch;
	short mess_type;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	max_groups=(*env)->GetIntField(env, info, fid_Envelope_max_groups);

	{
		/* We use a nested block to allocate an array for group names. */
		char groups[max_groups][MAX_GROUP_NAME];
		ret=SP_receive(mbox, &service_type, sender, max_groups, &num_groups, groups,
						&mess_type, &endian_mismatch, len, buf+pos);
		
		if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
			(*env)->ReleaseByteArrayElements(env, marray, buf, 0);

		if (ret<0)
			return ret;

		grparr=(*env)->NewObjectArray(env, num_groups, (*env)->FindClass(env, "java/lang/String"), NULL);
		for(i=0;i<num_groups;i++)
			(*env)->SetObjectArrayElement(env, grparr, i, (*env)->NewStringUTF(env, groups[i]));
		(*env)->SetObjectField(env, info, fid_Envelope_recvr_names, grparr);
	}

	(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_set_position, pos+ret);

	(*env)->SetShortField(env, info, fid_Envelope_mess_type, mess_type);
	(*env)->SetIntField(env, info, fid_Envelope_service_type, service_type);
	(*env)->SetIntField(env, info, fid_Envelope_endian_mismatch, endian_mismatch);
	(*env)->SetObjectField(env, info, fid_Envelope_sender, (*env)->NewStringUTF(env, sender));

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_SpMailbox_C_1parseView
  (JNIEnv *env, jobject self, jobject info, jobject rinfo, jobject mess) {
	void* buf;
	membership_info minfo;
	int i, len, pos, limit;
	int ret=-1;
	jarray marray;
	int service;
	jarray gidarray;
	char gidstr[27];
	jobjectArray grparr;

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	service=(*env)->GetIntField(env, rinfo, fid_Envelope_service_type);
	if ((ret=SP_get_memb_info(buf+pos, service, &minfo))<0)
		goto out;

	gidarray=(*env)->NewIntArray(env, 3);
	(*env)->SetIntArrayRegion(env, gidarray, 0, 3, minfo.gid.id);
	(*env)->SetObjectField(env, info, fid_View_group_id, gidarray);

	if (service&CAUSED_BY_NETWORK) {
		/* We use a nested block to allocate an array for group names. */
		char vs_set[MAX_GROUP_NAME][minfo.my_vs_set.num_members];

		if ((ret=SP_get_vs_set_members(buf, &minfo.my_vs_set, vs_set, minfo.my_vs_set.num_members))<0)
			goto out;

		grparr=(*env)->NewObjectArray(env, minfo.my_vs_set.num_members, (*env)->FindClass(env, "java/lang/String"), NULL);
		for(i=0;i<minfo.my_vs_set.num_members;i++)
			(*env)->SetObjectArrayElement(env, grparr, i, (*env)->NewStringUTF(env, vs_set[i]));
	} else {
		grparr=(*env)->NewObjectArray(env, 1, (*env)->FindClass(env, "java/lang/String"), NULL);
		(*env)->SetObjectArrayElement(env, grparr, 0, (*env)->NewStringUTF(env, minfo.changed_member));
	}
	(*env)->SetObjectField(env, info, fid_View_vs_set, grparr);

	ret=0;

 out:
	if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		(*env)->ReleaseByteArrayElements(env, marray, buf, JNI_ABORT);

	return ret;
}

/* FlushSpread */

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1connect
  (JNIEnv *env, jobject self, jstring spread_name, jstring private_name,
   jboolean priority, jboolean membership) {
	const char *spread, *private=NULL;
	int mbox, ret;
	jstring gstr;
	char gid[MAX_GROUP_NAME];

	spread=(*env)->GetStringUTFChars(env, spread_name, NULL);
	if (private_name!=NULL)
		private=(*env)->GetStringUTFChars(env, private_name, NULL);

	ret=FL_connect(spread, private, priority, &mbox, gid);

	(*env)->ReleaseStringUTFChars(env, spread_name, spread);
	if (private!=NULL)
		(*env)->ReleaseStringUTFChars(env, private_name, private);

	(*env)->SetIntField(env, self, fid_Mailbox_mailbox, mbox);

	if (ret>0) {
		gstr=(*env)->NewStringUTF(env, gid);
		(*env)->SetObjectField(env, self, fid_Mailbox_private_group, gstr);
	}

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1disconnect
  (JNIEnv *env, jobject self) {
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	ret=FL_disconnect(mbox);

	(*env)->SetIntField(env, self, fid_Mailbox_mailbox, -1);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1join
  (JNIEnv *env, jobject self, jstring group) {
	const char* grp;
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);
	grp=(*env)->GetStringUTFChars(env, group, NULL);

	ret=FL_join(mbox, grp);

	(*env)->ReleaseStringUTFChars(env, group, grp);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1leave
  (JNIEnv *env, jobject self, jstring group) {
	const char* grp;
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);
	grp=(*env)->GetStringUTFChars(env, group, NULL);

	ret=FL_leave(mbox, grp);

	(*env)->ReleaseStringUTFChars(env, group, grp);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1flush
  (JNIEnv *env, jobject self, jstring group) {
	const char* grp;
	int mbox, ret;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);
	grp=(*env)->GetStringUTFChars(env, group, NULL);

	ret=FL_flush(mbox, grp);

	(*env)->ReleaseStringUTFChars(env, group, grp);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1subgroupcast
  (JNIEnv *env, jobject self, jobject info, jobject mess) {
	int i;
	const char* grp;
	void* buf;
	jstring group;
	jobject grouparr, marray;
	int mbox, mess_type, service_type, ret, pos, len;
	int num_groups;
	char group_name[MAX_GROUP_NAME];

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	mess_type=(*env)->GetIntField(env, info, fid_Envelope_mess_type);
	service_type=(*env)->GetIntField(env, info, fid_Envelope_service_type);
	group=(*env)->GetObjectField(env, info, fid_Envelope_group_name);
	if (group==NULL)
		return ILLEGAL_GROUP;
	grp=(*env)->GetStringUTFChars(env, group, NULL);
	strcpy(group_name, grp);
	(*env)->ReleaseStringUTFChars(env, group, grp);
	grouparr=(*env)->GetObjectField(env, info, fid_Envelope_recvr_names);
	if (grouparr==NULL)
		return ILLEGAL_GROUP;
	num_groups=(*env)->GetArrayLength(env, grouparr);

	{
		/* We use a nested block to allocate an array for group names. */
		char groups[num_groups][MAX_GROUP_NAME];
		for(i=0;i<num_groups;i++) {
			jobject grpelem=(*env)->GetObjectArrayElement(env, grouparr, i);
			if (grpelem==NULL)
				return ILLEGAL_GROUP;
			grp=(*env)->GetStringUTFChars(env, (*env)->GetObjectArrayElement(env, grouparr, i), NULL);
			strcpy(groups[i], grp);
			(*env)->ReleaseStringUTFChars(env, group, grp);
		}

		ret=FL_subgroupcast(mbox, service_type, group_name, num_groups, groups, mess_type, len, buf+pos);
	}

 	if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
 		(*env)->ReleaseByteArrayElements(env, marray, buf, JNI_ABORT);

	if (ret<0)
		return ret;

	(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_set_position, pos+ret);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1multicast
  (JNIEnv *env, jobject self, jobject info, jobject mess) {
	int i;
	const char* grp;
	void* buf;
	jstring group;
	jobject grouparr, marray;
	int mbox, mess_type, service_type, ret, pos, len;
	int num_groups;
	char group_name[MAX_GROUP_NAME];

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	mess_type=(*env)->GetIntField(env, info, fid_Envelope_mess_type);
	service_type=(*env)->GetIntField(env, info, fid_Envelope_service_type);
	group=(*env)->GetObjectField(env, info, fid_Envelope_group_name);
	if (group==NULL)
		return ILLEGAL_GROUP;
	grp=(*env)->GetStringUTFChars(env, group, NULL);
	strcpy(group_name, grp);
	(*env)->ReleaseStringUTFChars(env, group, grp);

	ret=FL_multicast(mbox, service_type, group_name, mess_type, len, buf+pos);

 	if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
 		(*env)->ReleaseByteArrayElements(env, marray, buf, JNI_ABORT);

	if (ret<0)
		return ret;

	(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_set_position, pos+ret);

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1receive
  (JNIEnv *env, jobject self, jobject info, jobject mess) {
	const char* grp;
	void* buf;
	int mbox, ret, len, pos, i;
	jobject marray;
	jobjectArray grparr;

	int service_type;
	char sender[MAX_GROUP_NAME];
	int max_groups, num_groups;
	int endian_mismatch, more_messes;
	short mess_type;

	mbox=(*env)->GetIntField(env, self, fid_Mailbox_mailbox);

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	max_groups=(*env)->GetIntField(env, info, fid_Envelope_max_groups);
	service_type=(*env)->GetIntField(env, info, fid_Envelope_service_type);

	{
		/* We use a nested block to allocate an array for group names. */
		char groups[max_groups][MAX_GROUP_NAME];
		ret=FL_receive(mbox, &service_type, sender, max_groups, &num_groups, groups,
						&mess_type, &endian_mismatch, len, buf+pos, &more_messes);

 		if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
 			(*env)->ReleaseByteArrayElements(env, marray, buf, 0);

		if (ret<0)
			return ret;

		grparr=(*env)->NewObjectArray(env, num_groups, (*env)->FindClass(env, "java/lang/String"), NULL);
		for(i=0;i<num_groups;i++)
			(*env)->SetObjectArrayElement(env, grparr, i, (*env)->NewStringUTF(env, groups[i]));
		(*env)->SetObjectField(env, info, fid_Envelope_recvr_names, grparr);
	}

	(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_set_position, pos+ret);

	(*env)->SetShortField(env, info, fid_Envelope_mess_type, mess_type);
	(*env)->SetIntField(env, info, fid_Envelope_service_type, service_type);
	(*env)->SetIntField(env, info, fid_Envelope_endian_mismatch, endian_mismatch);
	(*env)->SetObjectField(env, info, fid_Envelope_sender, (*env)->NewStringUTF(env, sender));

	return ret;
}

JNIEXPORT jint JNICALL Java_net_sf_jgcs_spread_jni_FlMailbox_C_1parseView
  (JNIEnv *env, jobject self, jobject info, jobject rinfo, jobject mess) {
	void* buf;
	int i, len, pos, limit;
	jobjectArray grparr;
	int num_groups;
	char (*vs_set)[MAX_GROUP_NAME];
	group_id* gid;
	char gidstr[27];
	int ret=-1;
	jarray marray;

	pos=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_position);
	len=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_get_limit)-pos;
	if ((*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		buf=(*env)->GetDirectBufferAddress(env, mess);
	else {
		marray=(*env)->CallObjectMethod(env, mess, mid_ByteBuffer_array);
		pos+=(*env)->CallIntMethod(env, mess, mid_ByteBuffer_arrayOffset);
		buf=(*env)->GetByteArrayElements(env, marray, NULL);
	}

	if (FL_get_num_vs_offset_memb_mess()+sizeof(int)>len)
		goto out;

	num_groups=*(int*)(buf+pos+FL_get_num_vs_offset_memb_mess());

	if (FL_get_vs_set_offset_memb_mess()+(MAX_GROUP_NAME*num_groups)>len)
		goto out;

	vs_set=(char(*)[MAX_GROUP_NAME])(buf+pos+FL_get_vs_set_offset_memb_mess());

	grparr=(*env)->NewObjectArray(env, num_groups, (*env)->FindClass(env, "java/lang/String"), NULL);
	for(i=0;i<num_groups;i++)
		(*env)->SetObjectArrayElement(env, grparr, i, (*env)->NewStringUTF(env, vs_set[i]));
	(*env)->SetObjectField(env, info, fid_View_vs_set, grparr);
	
	gid=(group_id*)(buf+pos+FL_get_gid_offset_memb_mess());
	sprintf(gidstr,"%08x-%08x-%08x",gid->id[0],gid->id[1],gid->id[2]);
	(*env)->SetObjectField(env, info, fid_View_group_id, (*env)->NewStringUTF(env, gidstr));

	ret=0;

 out:
	if (!(*env)->CallBooleanMethod(env, mess, mid_ByteBuffer_isDirect))
		(*env)->ReleaseByteArrayElements(env, marray, buf, JNI_ABORT);

	return ret;
}

