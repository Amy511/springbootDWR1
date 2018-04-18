package com.example.demo;

import java.util.Collection;

import javax.servlet.ServletException;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

@RemoteProxy(name="MessagePush") 
public class MessagePush {  
    
	@RemoteMethod  
    public void onPageLoad(String userId) {  
  
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();  
  
        scriptSession.setAttribute("userId", userId);  
  
        DwrScriptSessionManagerUtil dwrScriptSessionManagerUtil = new DwrScriptSessionManagerUtil();  
  
        try {  
  
            dwrScriptSessionManagerUtil.init();  
  
        } catch (ServletException e) {  
  
            e.printStackTrace();  
  
        }  
  
    }
	
	@RemoteMethod	
public static void sendMessage(String userid, String message) {  
        
        final String userId = userid;  
  
        final String autoMessage = message;  
        final String OP_ID = "userId";  
          
        Browser.withAllSessionsFiltered(new ScriptSessionFilter() {  
  
            public boolean match(ScriptSession session) {  
                if (session.getAttribute(OP_ID) == null)  
                    return false;  
                else {  
                    boolean f = session.getAttribute(OP_ID).equals(userId);  
                    return f;  
                }  
            }  
  
        }, new Runnable() {  
  
            private ScriptBuffer script = new ScriptBuffer();  
  
            public void run() {  
  // push message
                script.appendCall("receiveMsg", autoMessage);  
                Collection<ScriptSession> sessions = Browser.getTargetSessions();  
                for (ScriptSession scriptSession : sessions) {  
                    scriptSession.addScript(script);  
                }  
  
            }  
  
        });  
  
    }  
	
	@RemoteMethod 
	public String sayHello(String name){
		System.out.println("hello " + name);  
        return "hello: "+name;  
	}
}  
