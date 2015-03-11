//Aleksandar Petrovic

package beans;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import user.UserInfo;

@Stateless
@Remote(UserManagementI.class)
public class UserManagementEJB implements UserManagementI {

	@PersistenceContext(name = "Server")
	private EntityManager em;

	@Override
	public void register(String id, String pass) {
		UserInfo user=new UserInfo();
		user.setUsername(id);
		user.setPass(pass);
		try
		{
			em.persist(user);
		} catch (EntityExistsException ex)
		{
			throw new IllegalArgumentException("User already exists.");
		} 
	}

}
