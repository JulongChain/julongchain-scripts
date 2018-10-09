package example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import shim.ISmartContractStub;
import shim.SmartContractBase;
import java.util.List;


import static java.lang.String.format;

public class Test01 extends SmartContractBase {

    private static Log log = LogFactory.getLog(Test01.class);

    @Override
    public SmartContractResponse init(ISmartContractStub stub) {
        log.info("===============================>>>> test init");
        try {
            final String function = stub.getFunction();
            System.out.println(function);
            final String[] args = stub.getParameters().toArray(new String[0]);
            if (args.length != 4) throw new IllegalArgumentException("Incorrect parameter format");
            for (int i = 0; i < args.length; i++) {
                System.out.println("args" + i + ": " + args[i]);
            }

            stub.putState(args[0], args[1].getBytes());
            stub.putState(args[2], args[3].getBytes());
        } catch (Throwable e) {
        return newErrorResponse(e);
    }
        return newSuccessResponse("InitSuccess");
    }

    @Override
    public SmartContractResponse invoke(ISmartContractStub stub) {
        log.info("================================>>>> test invoke");
	List<String> stringArgs = stub.getStringArgs();
	log.info(stringArgs.toString());
        log.info("================================");
        final String function = stub.getFunction();
        System.out.println(function);
        final String[] args = stub.getParameters().toArray(new String[0]);
        System.out.println(args.toString());
        switch (function) {
            case "move":
                return move(stub, args);
            case "query":
                return query(stub, args);
            default:
                return newErrorResponse(format("unknown function: %s", function));
        }
    }

    @Override
    public String getSmartContractStrDescription() {
        return "Test";
    }

    // 转账
    private SmartContractResponse move(ISmartContractStub stub, String[] args) {
        log.info("================================>>>> test move");

        if (args.length != 3) throw new IllegalArgumentException("Parameter error");
        // 转账，Ａ转账给Ｂ，金额Ｃ
        final String fromKey = args[0]; // A
        final String toKey = args[1]; // B
        final String amount = args[2]; // C

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
            throw new IllegalArgumentException("Lack of funds");
        }

        // 转账操作
        log.info(format("%s transfer %f to %s", fromKey, transferAmount, toKey));
        Double newFromAccountBalance = fromAccountBalance - transferAmount;
        Double newToAccountBalance = toAccountBalance + transferAmount;
        log.info(format("balance: %s = %f, %s = %f", fromKey, newFromAccountBalance, toKey, newToAccountBalance));
        stub.putStringState(fromKey, Double.toString(newFromAccountBalance));
        stub.putStringState(toKey, Double.toString(newToAccountBalance));

        return newSuccessResponse(format("Successful transfer %f ,", transferAmount));

    }

    // 查询
    private SmartContractResponse query(ISmartContractStub stub, String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Parameter error");
        final String accountKey = args[0];
        double amount = Double.parseDouble(stub.getStringState(accountKey));
        String message ="[{\"name\":\""+accountKey+"\",\"value\":"+amount+"}]";
        return newSuccessResponse(message);
    }
}
