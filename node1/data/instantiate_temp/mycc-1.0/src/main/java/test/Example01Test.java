package test;

import example.Example01;
import org.junit.Test;
import shim.ISmartContract;
import shim.ISmartContractStub;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example Test
 *
 * @author lishaojie
 * @date 2018/06/6
 * @company Dingxuan
 */
public class Example01Test {
    private ISmartContractStub stub = mock(ISmartContractStub.class);

    @Test
    public void initBidder() {
        String[] Init={"lv","10.0","liu","200.0"};
        List<String> list= Arrays.asList(Init);

        when(stub.getFunction()).thenReturn("initBidder");
        when(stub.getParameters()).thenReturn(list);

        Example01 t=new Example01();
        ISmartContract.SmartContractResponse smartContractResponse=t.init(stub);
//
//        System.out.println(smartContractResponse.getMessage());
//        System.out.println(smartContractResponse.getStatus());
        assertThat(smartContractResponse.getStatus(), is(ISmartContract.SmartContractResponse.Status.SUCCESS));

    }

    @Test
    public void initAuction() {

        String[] Init={"true","200.0","AAA","320.0"};
        List<String> list=Arrays.asList(Init);

        when(stub.getStringState("auctionstart")).thenReturn("2018-6-21 10:02:12");
        when(stub.getStringState("endtime")).thenReturn("2018-6-25 12:20:12");
        when(stub.getStringState("state")).thenReturn("true","true");
        when(stub.getStringState("HighestBidder")).thenReturn("liu:1.234");
        when(stub.getFunction()).thenReturn("initAuction");
        when(stub.getParameters()).thenReturn(list);

        Example01 t=new Example01();
        ISmartContract.SmartContractResponse smartContractResponse=t.init(stub);

        System.out.println(smartContractResponse.getMessage());
        System.out.println(smartContractResponse.getStatus());
        //assertThat(smartContractResponse.getStatus(), is(ISmartContract.SmartContractResponse.Status.SUCCESS));

    }


    @Test
    public void invokeInitCommodity() {
        String[] Init={"lv","10.0","liu","200.0"};
        List<String> list=Arrays.asList(Init);
        when(stub.getFunction()).thenReturn("initCommodity");
        when(stub.getParameters()).thenReturn(list);

        Example01 t=new Example01();
        ISmartContract.SmartContractResponse smartContractResponse=t.invoke(stub);

        assertThat(smartContractResponse.getStatus(), is(ISmartContract.SmartContractResponse.Status.SUCCESS));
    }

    @Test
    public void invokeInvoke() {
        String[] Init={"lv","liu","200.0"};
        List<String> list=Arrays.asList(Init);
        when(stub.getFunction()).thenReturn("invoke");
        when(stub.getParameters()).thenReturn(list);
        when(stub.getStringState("lv")).thenReturn("500.0");
        when(stub.getStringState("liu")).thenReturn("500.0");

        Example01 t=new Example01();
        ISmartContract.SmartContractResponse smartContractResponse=t.invoke(stub);
//
//        System.out.println(smartContractResponse.getMessage());
//        System.out.println(smartContractResponse.getStatus());
        assertThat(smartContractResponse.getStatus(), is(ISmartContract.SmartContractResponse.Status.SUCCESS));
    }

    @Test
    public void invokeQuery() {
        String[] Init={"lv"};
        List<String> list=Arrays.asList(Init);
        when(stub.getFunction()).thenReturn("query");
        when(stub.getParameters()).thenReturn(list);
        when(stub.getStringState("lv")).thenReturn("500.0");

        Example01 t=new Example01();
        ISmartContract.SmartContractResponse smartContractResponse=t.invoke(stub);
//        String a="1";
//        try {
//            a = new String(smartContractResponse.getPayload(),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        System.out.println(smartContractResponse.getMessage());
        assertThat(smartContractResponse.getStatus(), is(ISmartContract.SmartContractResponse.Status.SUCCESS));
    }

    @Test
    public void invokeGetBid() {
        String[] Init={"lv","10.0","liu","200.0"};
        List<String> list=Arrays.asList(Init);
        when(stub.getFunction()).thenReturn("getBid");
        when(stub.getParameters()).thenReturn(list);
        when(stub.getStringState("auctionstart")).thenReturn("2018-6-21 10:02:12");
        when(stub.getStringState("endtime")).thenReturn("2018-6-23 12:20:12");
        when(stub.getStringState("state")).thenReturn("true","true");
        when(stub.getStringState("HighestBidder")).thenReturn("liu:1.234");

        Example01 t=new Example01();
        ISmartContract.SmartContractResponse smartContractResponse=t.invoke(stub);
//        System.out.println(smartContractResponse.getMessage());
        System.out.println(smartContractResponse.getStatus());
//        assertThat(smartContractResponse.getStatus(), is(ISmartContract.SmartContractResponse.Status.SUCCESS));
    }

    @Test
    public void getSmartContractStrDescription() {

    }
}
