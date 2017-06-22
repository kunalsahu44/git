package test.mail;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;

// Example SNS Sender
public class AmazonSNSSender {

   	public Object handleRequest(S3Event input, Context context) {	    AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials("ACCESS_KEYS", "SECRET_KEY"));        

	    for (S3EventNotificationRecord record : input.getRecords()) {
	        String s3Key = record.getS3().getObject().getKey();
	        String s3Bucket = record.getS3().getBucket().getName();
	        context.getLogger().log("found id: " + s3Bucket+" "+s3Key);
/*	        // retrieve s3 object
	        S3Object object = s3Client.getObject(new GetObjectRequest(s3Bucket, s3Key));
	        InputStream objectData = object.getObjectContent();
*/	    

        // Create a client
        AmazonSNSClient service = new AmazonSNSClient(new BasicAWSCredentials("ACCESS_KEYS", "SECRET_KEY+O"));

        // Create a topic
        CreateTopicRequest createReq = new CreateTopicRequest()
            .withName("lamda");
        CreateTopicResult createRes = service.createTopic(createReq);

       

            // Publish to a topic
            PublishRequest publishReq = new PublishRequest()
                .withTopicArn(createRes.getTopicArn())
                .withMessage("Object with reference :\t " + s3Key +"has been uploaded in bucket:\t "+s3Bucket);
            service.publish(publishReq);
			

    }
		return s3Client;
}
}