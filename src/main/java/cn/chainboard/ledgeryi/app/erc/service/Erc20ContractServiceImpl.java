package cn.chainboard.ledgeryi.app.erc.service;

import cn.chainboard.ledgeryi.app.erc.TokenInfo;
import cn.ledgeryi.sdk.common.utils.ByteUtil;
import cn.ledgeryi.sdk.common.utils.DecodeUtil;
import cn.ledgeryi.sdk.contract.compiler.exception.ContractException;
import cn.ledgeryi.sdk.exception.CreateContractExecption;
import cn.ledgeryi.sdk.serverapi.data.DeployContractParam;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import cn.ledgeryi.sdk.serverapi.data.TriggerContractReturn;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor //for test
public class Erc20ContractServiceImpl implements IContractService {

    @Resource
    private LedgerYiService ledgerYiService;

    @Override
    public DeployContractReturn deployContract(String ownerAddress, String privateKey, TokenInfo tokenInfo) {
        return erc20Contract(ownerAddress, privateKey , tokenInfo);
    }

    /**
     * 同质化合约
     * @param ownerAddress 合约拥有者地址
     * @param privateKey 拥有者私钥
     * @param tokenInfo 合约参数
     * @return DeployContractReturn
     */
    private DeployContractReturn erc20Contract(String ownerAddress, String privateKey, TokenInfo tokenInfo){
        DeployContractParam result;
        try {
            Path source = Paths.get("contract","erc20","EIP20.sol");
            result = ledgerYiService.compileContractFromFile(source,"EIP20");
        } catch (ContractException e) {
            log.error("contract compile error: " + e.getMessage());
            return null;
        }
        return compileAndDeployErc20Contract(ownerAddress, privateKey, tokenInfo, result);

    }

    /**
     * 编译和创建合约, ERC20
     * @param ownerAddress 合约所有者地址
     * @param privateKey 合约所有者的私钥
     * @return DeployContractReturn 合约创建结果
     */
    private DeployContractReturn compileAndDeployErc20Contract(String ownerAddress, String privateKey,
                                                               TokenInfo tokenInfo, DeployContractParam result){

        DeployContractReturn deployContract = null;
        try {
            result.setConstructor("constructor(uint256,string,uint8,string)");
            ArrayList<Object> args = Lists.newArrayList();
            args.add(tokenInfo.getTotalSupply());
            args.add(tokenInfo.getName());
            args.add(tokenInfo.getDecimals());
            args.add(tokenInfo.getSymbol());
            result.setArgs(args);
            deployContract = ledgerYiService.deployContract(DecodeUtil.decode(ownerAddress),
                    DecodeUtil.decode(privateKey), result);
        } catch (CreateContractExecption e){
            log.error("create contract error: " + e.getMessage());
        }
        return deployContract;

    }

    @Override
    public long balanceOf(String ownerAddress, String contractAddress, String privateKey) {
        List args = Arrays.asList(ownerAddress);
        String method = "balanceOf(address)";
        ByteString result = ledgerYiService.callConstantContract(ownerAddress,contractAddress,method,args,privateKey);
        return ByteUtil.byteArrayToLong(result.toByteArray());
    }

    @Override
    public long totalSupply(String ownerAddress, String contractAddress, String privateKey) {
        List args = Collections.EMPTY_LIST;
        String method = "totalSupply()";
        ByteString result = ledgerYiService.callConstantContract(ownerAddress, contractAddress, method, args, privateKey);
        return ByteUtil.byteArrayToLong(result.toByteArray());
    }

    @Override
    public TriggerContractReturn burn(String ownerAddress, String contractAddress, String privateKey, long num) {
        return null;
    }

    @Override
    public TriggerContractReturn mint(String ownerAddress, String contractAddress, String privateKey, long num) {
        return null;
    }

    @Override
    public TriggerContractReturn transfer(String ownerAddress, String to, String contractAddress, String privateKey, long num) {
        List<Object> args = Arrays.asList(to, num);
        String method = "transfer(address,uint256)";
        return ledgerYiService.callContract(ownerAddress, contractAddress, method, args, privateKey);
    }

    @Override
    public TokenInfo getTokenInfo(String ownerAddress, String contractAddress, String privateKey){
        String symbol = "symbol()";
        ByteString resultSymbol = callConstantContract(symbol, ownerAddress, contractAddress, privateKey);
        String symbolResult = Strings.fromByteArray(resultSymbol.toByteArray()).trim();

        String name = "name()";
        ByteString resultName = callConstantContract(name, ownerAddress, contractAddress, privateKey);
        String nameResult = Strings.fromByteArray(resultName.toByteArray()).trim();

        String decimals = "decimals()";
        ByteString resultDecimals = callConstantContract(decimals, ownerAddress, contractAddress, privateKey);
        int decimalsResult = ByteUtil.byteArrayToInt(resultDecimals.toByteArray());

        long resultTotalSupply = totalSupply(ownerAddress, contractAddress, privateKey);
        return new TokenInfo(nameResult, symbolResult, resultTotalSupply, decimalsResult);
    }

    private ByteString callConstantContract(String method, String ownerAddress, String contractAddress, String privateKey){
        List args = Collections.EMPTY_LIST;
        return ledgerYiService.callConstantContract(ownerAddress, contractAddress, method, args, privateKey);
    }

    @Override
    public String ownerOfToken(String ownerAddress, String contractAddress, String privateKey, long tokenId) {
        return null;
    }

    @Override
    public long tokenByIndex(String ownerAddress, String contractAddress, String privateKey, long tokenIndex) {
        return 0;
    }
}
