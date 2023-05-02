package dslabs.clientserver;

import dslabs.atmostonce.AMOApplication;
import dslabs.atmostonce.AMOResult;
import dslabs.framework.Address;
import dslabs.framework.Application;
import dslabs.framework.Node;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Simple server that receives requests and returns responses.
 *
 * See the documentation of {@link Node} for important implementation notes.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class SimpleServer extends Node {
    // Your code here...
private final AMOApplication amoApplication;
    /* -------------------------------------------------------------------------
        Construction and Initialization
       -----------------------------------------------------------------------*/
    public SimpleServer(Address address, Application app) {
        super(address);

        // Your code here...
        amoApplication= new AMOApplication(app);
    }

    @Override
    public void init() {
        // No initialization necessary
    }

    /* -------------------------------------------------------------------------
        Message Handlers
       -----------------------------------------------------------------------*/
    private void handleRequest(Request m, Address sender) {
        // Your code here...'
       AMOResult amoResult = amoApplication.getHistory(m.command().address());
        if( amoResult!=null && m.sequenceNum() <= amoResult.sequence()) {
            Reply reply = new Reply(amoResult.result(), m.sequenceNum());
            send(reply,sender);
        }
        else{
        amoResult = amoApplication.execute(m.command());
        Reply reply = new Reply(amoResult.result(),m.sequenceNum());
        send(reply,sender);
        }
    }
}
