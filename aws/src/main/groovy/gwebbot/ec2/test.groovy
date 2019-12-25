package gwebbot.ec2
 
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client
import spock.lang.IgnoreIf
import spock.lang.Specification
 
class EC2UtilsSpec extends Specification {
 
    @IgnoreIf({ !System.properties['AWS_ACCESS_KEY_ID'] || 
                !System.properties['AWS_SECRET_KEY'] || 
                !System.properties['EC2_ENDPOINT'] })
    def "test start and stop a machine"() {
        given:
        AWSCreds credentials = new AWSCreds(access: System.properties['AWS_ACCESS_KEY_ID'],
                secret: System.properties['AWS_SECRET_KEY'])
 
        // http://docs.aws.amazon.com/general/latest/gr/rande.html#ec2_region
        String endpoint = System.properties['EC2_ENDPOINT']
        AmazonEC2 ec2client = new AmazonEC2Client(credentials)
        ec2client.endpoint = endpoint
        int fiveMinutes = 60 * 5 * 1_000
 
        when:
        String instanceName = 'Wolf1'
        String state = EC2Utils.instanceStateByName(ec2client, instanceName)
 
        then:
        notThrown AmazonServiceException
        state == 'stopped'
 
        when:
        EC2Utils.startInstanceWithName(ec2client, instanceName)
        sleep(fiveMinutes)
        state = EC2Utils.instanceStateByName(ec2client, instanceName)
 
        then:
        state == 'running'
 
        when:
        EC2Utils.stopInstanceWithName(ec2client, instanceName)
        sleep(fiveMinutes)
        state = EC2Utils.instanceStateByName(ec2client, instanceName)
 
        then:
        state == 'stopped'
    }
}