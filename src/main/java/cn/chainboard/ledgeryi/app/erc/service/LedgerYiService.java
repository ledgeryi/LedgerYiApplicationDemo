package cn.chainboard.ledgeryi.app.erc.service;

import cn.chainboard.ledgeryi.app.erc.ContractReceipt;
import cn.ledgeryi.protos.Protocol;
import cn.ledgeryi.protos.Protocol.Transaction.Result.ContractResult;
import cn.ledgeryi.protos.contract.SmartContractOuterClass;
import cn.ledgeryi.sdk.common.AccountYi;
import cn.ledgeryi.sdk.common.utils.DecodeUtil;
import cn.ledgeryi.sdk.common.utils.JsonFormatUtil;
import cn.ledgeryi.sdk.contract.compiler.exception.ContractException;
import cn.ledgeryi.sdk.exception.CreateContractExecption;
import cn.ledgeryi.sdk.serverapi.LedgerYiApiService;
import cn.ledgeryi.sdk.serverapi.data.DeployContractParam;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import cn.ledgeryi.sdk.serverapi.data.TriggerContractParam;
import cn.ledgeryi.sdk.serverapi.data.TriggerContractReturn;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.List;

/**
 * @author hq
 */
@Slf4j
@Component
public class LedgerYiService {

    private LedgerYiApiService ledgerYiApiService;

    @PostConstruct
    public void init(){
        ledgerYiApiService = new LedgerYiApiService();
    }

    /**
     * 创建默认账户
     * @return AccountYi 创建的默认账户
     */
    public AccountYi createLedgerYiAcconut(){
        return ledgerYiApiService.createDefaultAccount();
    }

    public String getContractFromOnChain(String contractAddress){
        SmartContractOuterClass.SmartContract contract = ledgerYiApiService.getContract(DecodeUtil.decode(contractAddress));
        System.out.println(JsonFormatUtil.printSmartContract(contract));
        JSONObject jsonObject = JSONObject.parseObject(JsonFormatUtil.printABI(contract.getAbi()));
        return jsonObject.getString("entrys");
    }

    public DeployContractParam compileContractFromFile(Path source, String contractName) throws ContractException {
        return ledgerYiApiService.compileContractFromFile(source,contractName);
    }

    public DeployContractReturn deployContract(byte[] ownerAddress, byte[] privateKey, DeployContractParam param) throws CreateContractExecption {
        return ledgerYiApiService.deployContract(ownerAddress,privateKey,param);
    }

    /**
     * 调用合约方法，会产生交易
     * @param ownerAddress 合约所有者地址
     * @param contractAddress 合约地址
     * @param method 合约方法
     * @param args 合约方法参数
     * @param privateKey 调用者私钥
     * @return boolean 该笔交易是否广播成功
     */
    public TriggerContractReturn callContract (String ownerAddress, String contractAddress, String method,
                                               List<Object> args, String privateKey) {
        return triggerContract(ownerAddress, contractAddress, method, args, false, privateKey);

    }

    /**
     * 调用查询合约方法，不产生交易（查询余额）
     *
     * @param ownerAddress 合约所有者地址
     * @param contractAddress 合约地址
     * @param method 合约方法
     * @param args 合约方法参数
     * @param privateKey 调用者私钥
     * @return ByteString 合约的查询结果
     */
    public ByteString callConstantContract (String ownerAddress, String contractAddress, String method,
                                            List<Object> args, String privateKey) {
        TriggerContractReturn triggerContractReturn = triggerContract(ownerAddress, contractAddress,
                method, args, true, privateKey);

        return triggerContractReturn.getCallResult();

    }

    private TriggerContractReturn triggerContract(String ownerAddress, String contractAddress, String method,
                                List<Object> args, boolean isConstant, String privateKey){
        TriggerContractParam triggerContractParam = new TriggerContractParam()
                .setContractAddress(DecodeUtil.decode(contractAddress))
                .setCallValue(0)
                .setConstant(isConstant)
                .setArgs(args)
                .setTriggerMethod(method);
        return ledgerYiApiService.triggerContract(DecodeUtil.decode(ownerAddress),
                DecodeUtil.decode(privateKey), triggerContractParam);
    }

    /**
     * 查询交易执行结果
     * @param txId 交易ID
     * @return ContractReceipt 交易执行结果
     */
    public ContractReceipt getTransactionInfo(String txId){
        Protocol.TransactionInfo information = ledgerYiApiService.getTransactionInfoById(txId);
        return ContractReceipt.builder()
                .execResult(ContractResult.SUCCESS.name().equalsIgnoreCase(information.getReceipt().getResult().name()))
                .contractResult(information.getReceipt().getResult())
                .reason(DecodeUtil.createReadableString(information.getResMessage()))
                .build();
    }
}