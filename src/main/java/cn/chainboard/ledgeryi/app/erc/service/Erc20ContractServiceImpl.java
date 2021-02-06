package cn.chainboard.ledgeryi.app.erc.service;

import cn.chainboard.ledgeryi.app.erc.TokenInfo;
import cn.ledgeryi.sdk.common.utils.ByteUtil;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import cn.ledgeryi.sdk.serverapi.data.TriggerContractReturn;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import org.spongycastle.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor //for test
public class Erc20ContractServiceImpl implements IContractService {

    @Resource
    private LedgerYiService ledgerYiService;

    @Override
    public DeployContractReturn deployContract(String ownerAddress, String privateKey, TokenInfo tokenInfo) {
        return ledgerYiService.erc20Contract(ownerAddress, privateKey , tokenInfo);
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
