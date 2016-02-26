package com.jiraservice.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;

import com.jiraservice.bean.SessionBean;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@ManagedBean(name="login")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 2174733404607546807L;
	
	private String pwd;
	private String msg;
	private String user;
	private String userDisplayName;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	//validate login
	public String validateUsernamePassword() {
		boolean valid = validateUser(this.user, this.pwd);
		if (valid) {
			HttpSession session = SessionBean.getSession();
			session.setAttribute("username", this.user);
			return "admin";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Usuário/Senha Incorreto",
							"Por favor insira um(a) Usuário/Senha válidos"));
			return "login";
		}
	}

	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionBean.getSession();
		session.invalidate();
		return "login";
	}
	
	public boolean validateUser(String name, String password){
		
		try {
			String url = "https://emcconsulting.atlassian.net/rest/api/latest/myself";
			String authString = name + ":" + password;
			String authStringEnc = new Base64().encodeToString(authString.getBytes());
			Client restaClient = Client.create();
			WebResource webResource = restaClient.resource(url);
			ClientResponse resp = webResource.accept("application/json")
			                                 .header("Authorization", "Basic " + authStringEnc)
			                                 .get(ClientResponse.class);
			if(resp.getStatus() == 200) {
				String outputJson = resp.getEntity(String.class);
				restaClient.destroy();
				JSONObject obj = new JSONObject(outputJson);
				String displayName = obj.getString("displayName");
				setUserDisplayName(displayName);
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
