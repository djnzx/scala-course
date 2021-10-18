/**
 * UserServicePort_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package amt.parts.generated;

public interface UserService extends java.rmi.Remote {

    /**
     * param array $TestQuerry data for oem
     */
    public Object[] getMultPrice(Object[] testQuerry, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param string $OemCode Code
     */
    public Object[] getPartsPrice(String oemCode, int makeId, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $DatePeriod period
     */
    public Object[] getPartsStatus(Object[] datePeriod, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param string $ClientOrderNum nomer zakaza
     */
    public String setOrderParam(String clientOrderNum, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $UserParam user login and passwd
     */
    public String sendOrder(Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $PartsArray PartsArray
     */
    public Object[] insertBasket2(Object[] partsArray, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param int $InvoiceNum invoice num
     */
    public Object[] getInvoiceDetails(int invoiceNum, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $UserParam user login and passwd
     */
    public Object[] getInvoiceList(Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param string $ClientOrderNum order num
     */
    public Object[] getOrderStatus(String clientOrderNum, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param string $Login client login
     */
    public String getClientId(String login, String passwd) throws java.rmi.RemoteException;

    /**
     * param array $OemCodes codes
     */
    public Object[] getPriceByOem(Object[] oemCodes, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $UserParam user login and passwd
     */
    public Object[] getBasketDetails(Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $UserParam user login and passwd
     */
    public String cleanBasket(Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $DatePeriod period
     */
    public Object[] getDetailStatusByOem(Object[] datePeriod, Object[] detailData, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $DetailData detail data
     */
    public Object[] getDetailStatusById(Object[] detailData, Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param array $UserParam user login and passwd
     */
    public Object[] viewBasket(Object[] userParam) throws java.rmi.RemoteException;

    /**
     * param string $Oem oem
     */
    public String deleteFromBasketByOem(String oem, Object[] userParam) throws java.rmi.RemoteException;
}
