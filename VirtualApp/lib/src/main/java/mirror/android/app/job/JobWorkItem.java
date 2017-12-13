package mirror.android.app.job;

import android.annotation.TargetApi;
import android.os.Build;

import mirror.RefClass;
import mirror.RefInt;
import mirror.RefObject;

/**
 * @author Lody
 */

@TargetApi(Build.VERSION_CODES.O)
public class JobWorkItem {
    public static Class<?> TYPE = RefClass.load(JobWorkItem.class, android.app.job.JobWorkItem.class);

    public static RefInt mWorkId;

    public static RefObject<Object> mGrants;

    public static RefInt mDeliveryCount;
}
