package com.vip.saturn.job.basic;

import com.vip.saturn.job.exception.SaturnJobException;
import com.vip.saturn.job.utils.AlarmUtils;
import com.vip.saturn.job.utils.UpdateJobCronUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.vip.saturn.job.utils.SystemEnvProperties.VIP_SATURN_DISABLE_CALLING_REST_API;

/**
 * Provide the hook for client job callback.
 */
public class SaturnApi {

	private static Logger log = LoggerFactory.getLogger(SaturnApi.class);

	private String namespace;

	private String executorName;

	public SaturnApi(String namespace, String executorName) {
		this.namespace = namespace;
		this.executorName = executorName;
	}

	// Make sure that only SaturnApi(String namespace) will be called.
	private SaturnApi() {
	}

	public void updateJobCron(String jobName, String cron, Map<String, String> customContext)
			throws SaturnJobException {
		if (VIP_SATURN_DISABLE_CALLING_REST_API) {
			log.info("Update job cron will be ignored as calling REST API is disabled.");
			return;
		}

		UpdateJobCronUtils.updateJobCron(namespace, jobName, cron, customContext);
	}

	/**
	 * The hook for client job raise alarm.
	 *
	 * @param alarmInfo The alarm information.
	 */
	public void raiseAlarm(Map<String, Object> alarmInfo) throws SaturnJobException {
		if (VIP_SATURN_DISABLE_CALLING_REST_API) {
			log.info("Raise alarm will be ignored as calling REST API is disabled.");
			return;
		}
		// set executorName into the alarmInfo
		alarmInfo.put("executorName", executorName);
		AlarmUtils.raiseAlarm(alarmInfo, namespace);
	}
}
