package com.spring.quartz;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.spring.quartz.config.QuartzConfig;
import com.spring.quartz.utils.BeanUtils;

//clustering 모드에선 아래 어노테이션이 동작하지 않음
//@DisallowConcurrentExecution
public class QuartzJob extends QuartzJobBean implements InterruptableJob {

   private static final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);
   private static final String JOB_NM = "jobNm";
   
   private volatile boolean isJobInterrupted = false; 
   private volatile Thread currThread;
   
   @Autowired
   private JobLauncher jobLauncher;
   
   /**
    * @description Quartz Job 실행 시 해당 executeInternal Mehtod가 수행
    * @exception JobExecutionException
    */
   @Override
   protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
      try {
         logger.info("executeInternal called ! ");
         
         String jobNm = context.getJobDetail().getJobDataMap().getString(JOB_NM);
         logger.info("{} started!", jobNm);
         
         //job 내의 파라미터가 모두 동일한 경우 1회만 실행되고 중복 job 으로 분류되어 실행이 불가.
         //currentTime을 파라미터에 추가하여 이를 방지.
         JobParametersBuilder jpb = new JobParametersBuilder();
         jpb.addLong("currTime", System.currentTimeMillis());
         
         jobLauncher.run((Job)BeanUtils.getBean(jobNm), jpb.toJobParameters());  //batch job 실행
         
      } catch (Exception e) {
         logger.error("ex in job execute: {}", e.getMessage());
      }
   }

   /**
    * @description Quartz Scheduler Gracefully Shutdown 시 Job의 해당 interrupt() Mehtod가 호출
    * @exception UnableToInterruptJobException
    */
   @Override
   public void interrupt() throws UnableToInterruptJobException {
      isJobInterrupted = true;
      if(currThread != null) {
         logger.info("interrupting-{"+currThread.getName()+"}");
         currThread.interrupt();
      }
   }
   
}
