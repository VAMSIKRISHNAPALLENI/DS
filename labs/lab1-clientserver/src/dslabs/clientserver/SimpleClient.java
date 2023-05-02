package dslabs.clientserver;
import dslabs.atmostonce.AMOCommand;
import dslabs.framework.Address;
import dslabs.framework.Client;
import dslabs.framework.Command;
import dslabs.framework.Node;
import dslabs.framework.Result;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import static dslabs.clientserver.ClientTimer.CLIENT_RETRY_MILLIS;

/**
 * Simple client that sends requests to a single server and returns responses.
 *
 * See the documentation of {@link Client} and {@link Node} for important
 * implementation notes.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class SimpleClient extends Node implements Client {
    private final Address serverAddress;
    private final Address clientAddress;
    // Your code here...
    private int sequenceNum=0;
    private Request request;
    private  Reply reply;
    /* -------------------------------------------------------------------------
        Construction and Initialization
       -----------------------------------------------------------------------*/
    public SimpleClient(Address address, Address serverAddress) {
        super(address);
        this.clientAddress=address;
        this.serverAddress = serverAddress;
    }

    @Override
    public synchronized void init() {
        // No initialization necessary
    }

    /* -------------------------------------------------------------------------
        Client Methods
       -----------------------------------------------------------------------*/
    @Override
    public synchronized void sendCommand(Command command) {
        // Your code here...
        sequenceNum++;
        reply = null;
        AMOCommand amoCommand = new AMOCommand(command,clientAddress,sequenceNum);
        Request r = new Request(amoCommand,sequenceNum);
        request =r;
        this.send(r,serverAddress);
        this.set(new ClientTimer(r),CLIENT_RETRY_MILLIS);
    }

    @Override
    public synchronized boolean hasResult() {
        // Your code here...
        if(reply==null)
            return false;
        return
                reply.sequenceNum()==sequenceNum;
    }

    @Override
    public synchronized Result getResult() throws InterruptedException {
        // Your code here...
        while(reply==null)
        {
            this.wait();
        }
        return reply.result();
    }

    /* -------------------------------------------------------------------------
        Message Handlers
       -----------------------------------------------------------------------*/
    private synchronized void handleReply(Reply m, Address sender) {
        // Your code here...
        if(Objects.equals(sequenceNum,m.sequenceNum())){
            reply=m;
            this.notify();
        }
    }

    /* -------------------------------------------------------------------------
        Timer Handlers
       -----------------------------------------------------------------------*/
    private synchronized void onClientTimer(ClientTimer t) {
        // Your code here...
        if(Objects.equals(sequenceNum,t.request().sequenceNum())){
            this.send(t.request(),serverAddress);
            this.set(new ClientTimer(t.request()),CLIENT_RETRY_MILLIS);
        }
    }
}
