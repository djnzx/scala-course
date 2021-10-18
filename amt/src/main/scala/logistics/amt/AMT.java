package logistics.amt;

import logistics.amt.generated.UserService;
import logistics.amt.generated.UserServiceServiceLocator;
import logistics.amt.number.NumberDetails;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AMT {
  private final UserService api;
  private final AMTAuth auth;

  public AMT(AMTAuth auth) throws ServiceException {
    this.api = new UserServiceServiceLocator().getUserServicePort();
    this.auth = auth;
  }

  /**
   * fetch NumberDetails for an ARRAY of part numbers
   */
  public Collection<NumberDetails> fetchNumberDetails(String[] query) throws RemoteException {
    Object[] multPrice = api.getPriceByOem(query, auth.credentials());
    return Arrays.stream(multPrice).map(NumberDetails::from).collect(Collectors.toList());
  }

  /**
   * fetch NumberDetails for an ANY ITERABLE<STRING> of part numbers
   */
  public Collection<NumberDetails> fetchNumberDetails(Iterable<String> query) throws RemoteException {
    return fetchNumberDetails(StreamSupport.stream(query.spliterator(), false).toArray(String[]::new));
  }

  /**
   * fetch NumberDetails for ONE part numbers
   */
  public Collection<NumberDetails> fetchNumberDetails(String query) throws RemoteException {
    return fetchNumberDetails(new String[] { query });
  }

}
