package com.lody.virtual.client.hook.proxies.job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.ipc.VJobScheduler;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.utils.ComponentUtils;

import java.lang.reflect.Method;

import mirror.android.app.job.IJobScheduler;

/**
 * @author Lody
 *
 * @see android.app.job.JobScheduler
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobServiceStub extends BinderInvocationProxy {

	public JobServiceStub() {
		super(IJobScheduler.Stub.asInterface, Context.JOB_SCHEDULER_SERVICE);
	}

	@Override
	protected void onBindMethods() {
		super.onBindMethods();
		addMethodProxy(new schedule());
		addMethodProxy(new getAllPendingJobs());
		addMethodProxy(new cancelAll());
		addMethodProxy(new cancel());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			addMethodProxy(new getPendingJob());
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			addMethodProxy(new enqueue());
		}
	}


	private class schedule extends MethodProxy {

		@Override
		public String getMethodName() {
			return "schedule";
		}

		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			JobInfo jobInfo = (JobInfo) args[0];
			return VJobScheduler.get().schedule(jobInfo);
		}
	}

	private class getAllPendingJobs extends MethodProxy {

		@Override
		public String getMethodName() {
			return "getAllPendingJobs";
		}

		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			return VJobScheduler.get().getAllPendingJobs();
		}
	}

	private class cancelAll extends MethodProxy {

		@Override
		public String getMethodName() {
			return "cancelAll";
		}

		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			VJobScheduler.get().cancelAll();
			return 0;
		}
	}

	private class cancel extends MethodProxy {

		@Override
		public String getMethodName() {
			return "cancel";
		}

		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			int jobId = (int) args[0];
			VJobScheduler.get().cancel(jobId);
			return 0;
		}
	}

	private class getPendingJob extends MethodProxy {

		@Override
		public String getMethodName() {
			return "getPendingJob";
		}

		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			int jobId = (int) args[0];
			return VJobScheduler.get().getPendingJob(jobId);
		}
	}

	private class enqueue extends MethodProxy {

		@Override
		public String getMethodName() {
			return "enqueue";
		}

		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			JobInfo jobInfo = (JobInfo) args[0];
			JobWorkItem workItem = redirect((JobWorkItem) args[1], getAppPkg());
			return VJobScheduler.get().enqueue(jobInfo, workItem);
		}
	}

	@TargetApi(Build.VERSION_CODES.O)
	private JobWorkItem redirect(JobWorkItem item, String pkg) {
		if (item != null) {
			Intent intent = ComponentUtils.redirectIntentSender(ActivityManagerCompat.INTENT_SENDER_SERVICE,
					pkg, item.getIntent());

			JobWorkItem workItem = new JobWorkItem(intent);
			int wordId = mirror.android.app.job.JobWorkItem.mWorkId.get(item);
			mirror.android.app.job.JobWorkItem.mWorkId.set(workItem, wordId);

			Object obj = mirror.android.app.job.JobWorkItem.mGrants.get(item);
			mirror.android.app.job.JobWorkItem.mGrants.set(workItem, obj);

			int deliveryCount = mirror.android.app.job.JobWorkItem.mDeliveryCount.get(item);
			mirror.android.app.job.JobWorkItem.mDeliveryCount.set(workItem, deliveryCount);
			return workItem;
		}
		return null;
	}
}
