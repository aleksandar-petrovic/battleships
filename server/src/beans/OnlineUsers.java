//Aleksandar Petrovic

package beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

@Singleton
@LocalBean
public class OnlineUsers{
		
		private Map<String, UserEJB> users;

		public OnlineUsers()
		{
			// svaki bean je posebna nit, pa moramo zastiti pristup kolekciji
			users = Collections.synchronizedMap(new HashMap<String, UserEJB>());
		}

		public void onLogin(String userId, UserEJB ejb)
		{
			users.put(userId, ejb);
		}

		public void onLogout(String userId)
		{
			users.remove(userId);
		}

		public Set<String> getOnline()
		{
			return new HashSet<>(users.keySet());
		}

		public UserEJB getEjb(String forUser)
		{
			return users.get(forUser);
		}

}
