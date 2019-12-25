package gwebbot.ec2
 
import com.amazonaws.auth.AWSCredentials
 
class AWSCreds implements AWSCredentials {
    String access
    String secret
 
    @Override
    String getAWSAccessKeyId() {
        access
    }
 
    @Override
    String getAWSSecretKey() {
        secret
    }
}