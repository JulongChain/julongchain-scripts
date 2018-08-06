/*copyright Dingxuan. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import shim.ISmartContractStub;
import shim.SmartContractBase;

import java.util.List;

import static java.lang.String.format;

/**
 * 类描述
 *
 * @author wanliangbing
 * @date 2018/05/26
 * @company Dingxuan
 */
public class MyCc extends SmartContractBase {

  private static Log log = LogFactory.getLog(MyCc.class);

  @Override
  public SmartContractResponse init(ISmartContractStub stub) {
    log.info("=================================================>>>> myCc init");
    List<byte[]> args = stub.getArgs();
    log.info(args.toString());
    log.info("args size:" + args.size());
    for (int i = 0; i < args.size(); i++) {
      System.out.println("args" + i + ": " + new String(args.get(i)));
    }
    stub.putState("a", "100".getBytes());
    stub.putState("b", "200".getBytes());
    return newSuccessResponse();
  }

  @Override
  public SmartContractResponse invoke(ISmartContractStub stub) {
    log.info("=================================================>>>> myCc invoke");
    List<byte[]> args1 = stub.getArgs();
    log.info("args1:" + args1.toString());
    log.info("args size:" + args1.size());

    final String function = stub.getFunction();
    final String[] args = stub.getParameters().toArray(new String[0]);

    log.info("function:" + function);
    stub.putState("a", "10".getBytes());
    stub.putState("b", "20".getBytes());
    //log.info("1:" + args[0]);
    //log.info("2:" + args[1]);
    //log.info("3:" + args[2]);

    return newSuccessResponse();
/**

 switch (function) {
 case "invoke":
 return invoke(stub, args);
 case "query":
 return query(stub, args);
 default:
 return newErrorResponse(format("未知方法: %s", function));
 }*/
  }

  @Override
  public String getSmartContractStrDescription() {
    return "MyCC";
  }

  // 转账
  private SmartContractResponse invoke(ISmartContractStub stub, String[] args) {
    if (args.length != 3) throw new IllegalArgumentException("参数错误");
    // 转账，Ａ转账给Ｂ，金额Ｃ
    final String fromKey = args[0]; // A
    final String toKey = args[1]; // B
    final String amount = args[2]; // C

    stub.putState(toKey, amount.getBytes());

    return newSuccessResponse();

/**

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
 throw new IllegalArgumentException("资金不足");
 }

 // 转账操作
 log.info(format("转账人：%s 转 %f 元给转账人：%s", fromKey, transferAmount, toKey));
 Double newFromAccountBalance = fromAccountBalance - transferAmount;
 Double newToAccountBalance = toAccountBalance + transferAmount;
 log.info(
 format(
 "各自余额为: %s = %f, %s = %f", fromKey, newFromAccountBalance, toKey, newToAccountBalance));
 stub.putStringState(fromKey, Double.toString(newFromAccountBalance));
 stub.putStringState(toKey, Double.toString(newToAccountBalance));
 log.info("转账结束.");

 return newSuccessResponse(format("成功转账 %f ,", transferAmount));

 */
  }

  // 查询
  private SmartContractResponse query(ISmartContractStub stub, String[] args) {
    if (args.length != 1) throw new IllegalArgumentException("参数错误");
    final String accountKey = args[0];
    double amount = Double.parseDouble(stub.getStringState(accountKey));
    return newSuccessResponse(format("成功查询 Name:" + accountKey + ", Amount:" + amount));
  }
}