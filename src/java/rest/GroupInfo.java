package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author plaul1
 */
@Path("ca2info")
public class GroupInfo {
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  public static List<String> urls = new ArrayList<String>() {
    {

      add("http://cphbusinessjb.cloudapp.net/CA2/");
      add("http://ca2-ebski.rhcloud.com/CA2New/");
      add("http://ca2-chrislind.rhcloud.com/CA2Final/");
      add("http://ca2-pernille.rhcloud.com/NYCA2/");
      add("https://ca2-jonasrafn.rhcloud.com:8443/company.jsp");
      add("http://ca2javathehutt-smcphbusiness.rhcloud.com/ca2/index.jsp");

      add("https://ca2-ssteinaa.rhcloud.com/CA2/");
      add("http://tomcat-nharbo.rhcloud.com/CA2/");
      add("https://ca2-cphol24.rhcloud.com/3.semCa.2/");
      add("https://ca2-ksw.rhcloud.com/DeGuleSider/");
      add("http://ca2-ab207.rhcloud.com/CA2/index.html");
      add("http://ca2-sindt.rhcloud.com/CA2/index.jsp");
      add("http://ca2gruppe8-tocvfan.rhcloud.com/");
      add("https://ca-ichti.rhcloud.com/CA2/");

      add("https://ca2-9fitteen.rhcloud.com:8443/CA2/");
      add("https://cagroup04-coolnerds.rhcloud.com/CA_v1/index.html");
      add("http://catwo-2ndsemester.rhcloud.com/CA2/");
    }
  };

  @Context
  private UriInfo context;

  /**
   * Creates a new instance of GroupInfo
   */
  public GroupInfo() {
  }

  /**
   * Retrieves representation of an instance of rest.GroupInfo
   *
   * @return an instance of java.lang.String
   * @throws java.lang.InterruptedException
   */
  @GET
  @Produces("application/json")
  public Response getJson() throws InterruptedException, ExecutionException {
    List<Future<JsonObject>> futures = new ArrayList<>(urls.size());
    JsonObject result = new JsonObject();
    result.addProperty("semester", "Fall 2015");
    JsonArray groups = new JsonArray();
    final ExecutorService service = Executors.newFixedThreadPool(10);

    try {
      for (String url : urls) {
        futures.add(service.submit(new GroupChecker(url)));
      }

      for (Future<JsonObject> future : futures) {
        try {
          groups.add(future.get(10, TimeUnit.SECONDS));
        } catch (TimeoutException ex) {
          Logger.getLogger(GroupInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

    } finally {
      service.shutdown();
    }
    result.add("groups", groups);
    return Response.ok(gson.toJson(result)).header("Content-type","application/json;charset=UTF-8").build();
  }

}
