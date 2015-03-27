package net.sf.jgcs.spread;

import net.sf.jgcs.tests.AfterClose;
import net.sf.jgcs.tests.JoinLeave;
import net.sf.jgcs.tests.Membership;
import net.sf.jgcs.tests.Messages;
import net.sf.jgcs.tests.OpenClose;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({OpenClose.class, Messages.class, JoinLeave.class, AfterClose.class, Membership.class})
public class BindingIT {

}
