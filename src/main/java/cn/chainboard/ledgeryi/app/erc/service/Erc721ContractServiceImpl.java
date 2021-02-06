package cn.chainboard.ledgeryi.app.erc.service;

import cn.chainboard.ledgeryi.app.erc.TokenInfo;
import cn.ledgeryi.sdk.common.utils.ByteUtil;
import cn.ledgeryi.sdk.common.utils.DecodeUtil;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import cn.ledgeryi.sdk.serverapi.data.TriggerContractReturn;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor //for test
public class Erc721ContractServiceImpl implements IContractService {

    @Resource
    private LedgerYiService ledgerYiService;

    @Override
    public DeployContractReturn deployContract(String ownerAddress, String privateKey, TokenInfo tokenInfo) {
        return ledgerYiService.erc721Contract(ownerAddress, privateKey , tokenInfo);
    }

    @Override
    public long balanceOf(String ownerAddress, String contractAddress, String privateKey) {
        List<Object> args = Arrays.asList(ownerAddress);
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
    public TriggerContractReturn burn(String ownerAddress, String contractAddress, String privateKey, long tokenId) {
        List<Object> args = Arrays.asList(tokenId);
        String method = "burnToken(uint256)";
        return ledgerYiService.callContract(ownerAddress, contractAddress, method, args, privateKey);
    }

    @Override
    public TriggerContractReturn mint(String ownerAddress, String contractAddress, String privateKey, long extraTokens) {
        List<Object> args = Arrays.asList(extraTokens);
        String method = "issueTokens(uint256)";
        return ledgerYiService.callContract(ownerAddress, contractAddress, method, args, privateKey);
    }

    @Override
    public TriggerContractReturn transfer(String ownerAddress, String to, String contractAddress, String privateKey, long tokenId) {
        List<Object> args = Arrays.asList(ownerAddress,to,tokenId);
        String method = "transferFrom(address,address,uint256)";
        return ledgerYiService.callContract(ownerAddress, contractAddress, method, args, privateKey);
    }

    @Override
    public TokenInfo getTokenInfo(String ownerAddress, String contractAddress, String privateKey) {
        String symbol = "symbol()";
        ByteString resultSymbol = callConstantContract(symbol, ownerAddress, contractAddress, privateKey);
        String symbolResult = Strings.fromByteArray(resultSymbol.toByteArray()).trim();

        String name = "name()";
        ByteString resultName = callConstantContract(name, ownerAddress, contractAddress, privateKey);
        String nameResult = Strings.fromByteArray(resultName.toByteArray()).trim();

        long resultTotalSupply = totalSupply(ownerAddress, contractAddress, privateKey);
        return new TokenInfo(nameResult, symbolResult, resultTotalSupply);
    }

    private ByteString callConstantContract(String method, String ownerAddress, String contractAddress, String privateKey){
        List args = Collections.EMPTY_LIST;
        return ledgerYiService.callConstantContract(ownerAddress, contractAddress, method, args, privateKey);
    }

    @Override
    public String ownerOfToken(String ownerAddress, String contractAddress, String privateKey, long tokenId) {
        List args = Arrays.asList(tokenId);
        String method = "ownerOf(uint256)";
        ByteString result = ledgerYiService.callConstantContract(ownerAddress, contractAddress, method, args, privateKey);
        return DecodeUtil.createReadableString(result).substring(23);
    }

    @Override
    public long tokenByIndex(String ownerAddress, String contractAddress, String privateKey, long tokenIndex) {
        List args = Arrays.asList(tokenIndex);
        String method = "tokenByIndex(uint256)";
        ByteString result = ledgerYiService.callConstantContract(ownerAddress, contractAddress, method, args, privateKey);
        return ByteUtil.byteArrayToLong(result.toByteArray());
    }
}
