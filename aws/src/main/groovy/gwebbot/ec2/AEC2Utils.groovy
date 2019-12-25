package gwebbot.ec2
 
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.StartInstancesRequest
import com.amazonaws.services.ec2.model.StopInstancesRequest
import com.amazonaws.services.ec2.model.Tag
 
class EC2Utils {
 
    static String instanceStateByName(AmazonEC2 ec2client, String intanceName) {
        for ( Reservation reservation : ec2client.describeInstances().reservations ) {
            Instance instance = reservation.instances.find { findInstanceNameWithTags(it) == intanceName }
            if (instance) {
                return instance.state.name
            }
        }
        null
    }
 
    static String instancePublicDnsNameByName(AmazonEC2 ec2client, String intanceName) {
        for ( Reservation reservation : ec2client.describeInstances().reservations ) {
            Instance instance = reservation.instances.find { findInstanceNameWithTags(it) == intanceName }
            if ( instance ) {
                return instance.publicDnsName
            }
        }
        null
    }
 
    static void startInstanceWithName(AmazonEC2 ec2client, String name) {
        for ( Reservation reservation : ec2client.describeInstances().reservations ) {
            Instance inst = reservation.instances.find {findInstanceNameWithTags(it) == name }
            if ( inst ) {
                ec2client.startInstances(new StartInstancesRequest([inst.instanceId]))
            }
        }
    }
    static void stopInstanceWithName(AmazonEC2 ec2client, String name) {
        for (Reservation reservation : ec2client.describeInstances().reservations) {
            Instance inst = reservation.instances.find {findInstanceNameWithTags(it) == name }
            if ( inst ) {
                ec2client.stopInstances(new StopInstancesRequest([inst.instanceId]))
            }
        }
    }
 
    static String findInstanceNameWithTags(Instance instance) {
        for ( Tag tag : instance.tags ) {
            if ( tag.key.toLowerCase() == 'name' ) {
                return tag.value
            }
        }
        null
    }
}