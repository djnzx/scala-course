package schedule.g

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import scala.jdk.CollectionConverters.SeqHasAsJava

object GoogleOAuthCredentials {

  // resources
  private val CRED_RESOURCE =
    "/client_secret_338997718172-iul7v5ae5ph1d6tci0rk8ovsb92lfcav.apps.googleusercontent.com.json"

  private val TOKENS_DIR = "tokens"

  @throws[IOException]
  def get(http: NetHttpTransport, json: JsonFactory, scopes: List[String]) = {

    val secrets = getClass.getResourceAsStream(CRED_RESOURCE) match {
      case null => throw new FileNotFoundException("Resource not found: " + CRED_RESOURCE)
      case in   => GoogleClientSecrets.load(json, new InputStreamReader(in))
    }

    val flow =
      new GoogleAuthorizationCodeFlow.Builder(
        http,
        json,
        secrets,
        scopes.asJava
      ).setDataStoreFactory(
        new FileDataStoreFactory(new File(TOKENS_DIR))
      ).setAccessType("offline").build

    val receiver = new LocalServerReceiver.Builder().setPort(8888).build

    new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
  }

}
