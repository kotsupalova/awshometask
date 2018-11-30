package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class DebugLambda implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    public DebugLambda() {}

    // Test purpose only.
    DebugLambda(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        System.out.println("SOUT 1. DebugLambda class ");
        context.getLogger().log("Received event: " + event);
        context.getLogger().log("LAMBDA FileName:" + event.getRecords().get(0).getS3().getObject().getKey());
        System.out.println("SOUT 2. DebugLambda class ");
        return "ok";
    }
}
