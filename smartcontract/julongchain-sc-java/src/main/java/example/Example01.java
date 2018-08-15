package example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import shim.ISmartContractStub;
import shim.SmartContractBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;

/**
 * Auction Example
 *
 * @author lishaojie
 * @date 2018/06/6
 * @company Dingxuan
 */
    public class Example01 extends SmartContractBase {

        private static Log log = LogFactory.getLog(Example01.class);

        public class temporaryRunnable implements Runnable {
            private ISmartContractStub stub;
            private String[] args;
            public void setStub(ISmartContractStub stub)
            {
                this.stub = stub;
            }
            public void setArgs(String[] args)
            {
                this.args = args;
            }
            @Override
            public void run() {
                try {
                    initAuction(stub,args);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public SmartContractResponse init(ISmartContractStub stub) {
            try {
                final String function = stub.getFunction();
                final String[] args = stub.getParameters().toArray(new String[0]);
                switch (function) {

                    case "initAuction"://初始化拍卖会

                        initTime(stub, args);

                        temporaryRunnable temporaryRunnable=new temporaryRunnable();
                        temporaryRunnable.setArgs(args);
                        temporaryRunnable.setStub(stub);
                        Thread temporaryThread = new Thread(temporaryRunnable);
                        temporaryThread.start();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date endtime = sdf.parse(Arrays.toString(stub.getState("endtime"))) ;
                        Date now = new Date();

                        long time = endtime.getTime()- now.getTime();

                        temporaryThread.sleep(10000);//实际使用时用time
                        return newSuccessResponse("Auction is going on");
                    default:
                        return newErrorResponse(format("Unknown parameter: %s", function));
                }
            } catch (Throwable e) {
                return newErrorResponse(e);
            }

        }

        //拍卖时间初始化
        private SmartContractResponse initTime(ISmartContractStub stub, String[] args) {
            if (args.length != 3) throw new IllegalArgumentException("Incorrect parameter format");

            String state=args[0];
            //校验数据是否正确

            String auctionstart=args[1];
            String endtime=args[2];

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date Dendtime=sdf.parse(endtime);
                Date Dauctionstart=sdf.parse(auctionstart);

                if (Dendtime.before(Dauctionstart)){
                    return newErrorResponse("End date must be later than start date");
                }

            }catch (Throwable e){
                return newErrorResponse("Date format is wrong");
            }

            stub.putStringState("state", state);
            stub.putStringState("auctionstart", auctionstart);
            stub.putStringState("endtime", endtime);

            return newSuccessResponse("Auction information entered successfully");
        }

        //拍卖会进程创建
        private void  initAuction(ISmartContractStub stub,String[] args) throws ParseException {

            if (args.length != 3) throw new IllegalArgumentException("Incorrect parameter format");


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            final Date auctionstart = sdf.parse(stub.getStringState("auctionstart"));
            final Date endtime=sdf.parse(stub.getStringState("endtime"));


            TimerTask task=new TimerTask(){
                @Override
                public void run() {
                    Date now = new Date();
                    if (now.after(auctionstart)) {
                        if (now.before(endtime)) {
                            setBid(stub,args);
                            System.out.println("running");
                        }else{
                            stub.putStringState("state", Boolean.toString(false));
                        }
                    }

                }
            };
            Timer time=new Timer();
            time.scheduleAtFixedRate(task,0,1000);
        }

        //功能入口
        @Override
        public SmartContractResponse invoke(ISmartContractStub stub) {

            try {
                final String function = stub.getFunction();
                final String[] args = stub.getParameters().toArray(new String[0]);

                switch (function) {
                    case "initBidder"://竞拍人信息录入
                        return initBidder(stub, args);
                    case "invoke"://转账功能
                        return invoke(stub, args);
                    case "query"://查询功能
                        return query(stub, args);
                    case "initCommodity"://商品录入
                        return initCommodity(stub, args);
                    case "getBid"://获取价格
                        return getBid(stub,args);
                    default:
                        return newErrorResponse(format("Unknown parameter: %s", function));
                }
            } catch (Throwable e) {
                return newErrorResponse(e);
            }

        }

        //转账
        private SmartContractResponse invoke(ISmartContractStub stub, String[] args) {
            if (args.length != 3) throw new IllegalArgumentException("Incorrect parameter format");
            //转账，Ａ转账给Ｂ，金额Ｃ
            final String fromKey = args[0];//A
            final String toKey = args[1];//B
            final String amount = args[2];//C

            // 获取身份信息
            final String fromKeyState = stub.getStringState(fromKey);
            final String toKeyState = stub.getStringState(toKey);

            // 转账人余额获取类型转换
            Double fromAccountBalance = Double.parseDouble(fromKeyState);
            Double toAccountBalance = Double.parseDouble(toKeyState);

            // 转账金额类型转换
            Double transferAmount = Double.parseDouble(amount);

            // 确保金额足够
            if (transferAmount > fromAccountBalance) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            // 转账操作
            log.info(format("Transfer: %s to %f to transfer: %s", fromKey,transferAmount, toKey ));
            Double newFromAccountBalance = fromAccountBalance - transferAmount;
            Double newToAccountBalance = toAccountBalance + transferAmount;
            log.info(format("The respective balances are: %s = %f, %s = %f", fromKey, newFromAccountBalance, toKey, newToAccountBalance));
            stub.putStringState(fromKey, Double.toString(newFromAccountBalance));
            stub.putStringState(toKey, Double.toString(newToAccountBalance));
            log.info("Transfer finished.");

            return newSuccessResponse(format("Successful transfer of %f ,",transferAmount));
        }

        //价格查询
        private SmartContractResponse getBid(ISmartContractStub stub, String[] args) throws ParseException {


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final Date endtime=sdf.parse(stub.getStringState("endtime"));
            Date now=new Date();

            String[] a=stub.getStringState("HighestBidder").split(":");
            String HighestBidder=a[0];
            Double HighestBid=Double.parseDouble(a[1]);
            if(now.after(endtime)){
                stub.putStringState("GoldenApple", HighestBidder);
                System.out.println("End of auction");
            }else{
                System.out.println(format("This maximum bid: %f ,Bidder: %s", HighestBid,HighestBidder));
            }

            return newSuccessResponse("Successful bid");
        }

        //查询
        private SmartContractResponse query(ISmartContractStub stub, String[] args) {
            if (args.length != 1) throw new IllegalArgumentException("Unknown parameter");

            final String accountKey = args[0];
            //账户信息查询：用户和余额
            JSONObject message= JSON.parseObject("{\"Name\":\""+accountKey.toUpperCase()+"\",\"Amount\":"+stub.getStringState(accountKey)+"}");
            return newSuccessResponse(message.toString());

        }


        //保存出价信息
        private void setBid(ISmartContractStub stub,String[] args){
            //初始化出价人及其出价　
            //args[0]="liu";args[1]="200.0";arg[2]="wang";args[3]="220.0";
            if (args.length != 4) throw new IllegalArgumentException("Unknown parameter");
            String HighestBidder="wu";
            HashMap<String,Double> BidderandBid=new HashMap<>();
            Double Bid1=Double.parseDouble(args[1]);
            Double Bid2=Double.parseDouble(args[3]);

            BidderandBid.put(args[0],Bid1);
            BidderandBid.put(args[2],Bid2);

            Collection<Double> c = BidderandBid.values();
            Object[] obj = c.toArray();
            Arrays.sort(obj);

            //排序
            Double HighestBid=(Double)obj[obj.length-1];
            for (Map.Entry<String,Double> m :BidderandBid.entrySet())  {
                //System.out.println(m.getKey()+"\t"+m.getValue());
                if(m.getValue().equals(HighestBid)){
                    HighestBidder=m.getKey();
                }
            }

            String RetrurnHighestBid=HighestBidder+":"+HighestBid.toString();
            //System.out.println(RetrurnHighestBid);
            stub.putStringState("HighestBidder", RetrurnHighestBid);

        }

        //商品属性
        public class Commodity{
            private String name;
            private String attribute1;
            private String attribute2;
            private String owner;
            //可补充其他属性

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAttribute1() {
                return attribute1;
            }

            public void setAttribute1(String attribute1) {
                this.attribute1 = attribute1;
            }

            public String getAttribute2() {
                return attribute2;
            }

            public void setAttribute2(String attribute2) {
                this.attribute2 = attribute2;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

        }

        //商品录入
        private SmartContractResponse initCommodity(ISmartContractStub stub,String[] args){
            if (args.length != 4) throw new IllegalArgumentException("Unknown parameter");

            Commodity Commodity=new Commodity();
            Commodity.setName(args[0]) ;
            Commodity.setAttribute1(args[1]);
            Commodity.setAttribute2(args[2]);
            Commodity.setOwner(args[3]);

            JSONObject jsonCommodity=(JSONObject)JSONObject.toJSON(Commodity);

            if(stub.getState(Commodity.getName())!=null){
                return newErrorResponse(format("The product already exists: %s", Commodity.getName()));
            }else{
                stub.putStringState(Commodity.getName(), jsonCommodity.toString());

                return newSuccessResponse(format("Product entry successful,owner: %s",Commodity.getOwner()));
            }

        }

        //竞拍人信息
        public class Bidder{

            private String name;
            private String balance;
            private String attribute1;
            private String attribute2;
            //可补充如电话号码,联系地址,担保人等信息.

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getAttribute1() {
                return attribute1;
            }

            public void setAttribute1(String attribute1) {
                this.attribute1 = attribute1;
            }

            public String getAttribute2() {
                return attribute2;
            }

            public void setAttribute2(String attribute2) {
                this.attribute2 = attribute2;
            }
        }

        //竞拍人信息录入
        private SmartContractResponse initBidder(ISmartContractStub stub,String[] args){

            if (args.length != 4) throw new IllegalArgumentException("Unknown parameter");

            Bidder Bidder=new Bidder();
            Bidder.setName(args[0]);
            Bidder.setBalance(args[1]);
            Bidder.setAttribute1(args[2]);
            Bidder.setAttribute2(args[3]);

            JSONObject jsonBidder=(JSONObject)JSONObject.toJSON(Bidder);
            // System.out.println(stub.getState(Bidder.getName()));
            if(stub.getState(Bidder.getName())!=null){
                return newErrorResponse(format("The bidder already exists: %s", Bidder.getName()));
            }else{
                stub.putStringState(Bidder.getName(), jsonBidder.toString());
                return newSuccessResponse(format("Bidders entered information successfully : %s",Bidder.getName()));
            }
        }

        @Override
        public String getSmartContractStrDescription() {
            return null;
        }
}
