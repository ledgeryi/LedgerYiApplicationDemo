package ledgeryi.app.erc20;

import cn.chainboard.ledgeryi.app.erc.TokenInfo;
import cn.chainboard.ledgeryi.app.erc.service.Erc20ContractServiceImpl;
import cn.chainboard.ledgeryi.app.erc.service.LedgerYiService;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import org.junit.Before;
import org.junit.Test;

public class ERC20Test {

    private Erc20ContractServiceImpl erc20ContractService;
    private LedgerYiService ledgerYiService;

    private static String ownerAddress = "99b8466efe9f05cee87d4e167cdfaec0432d90fc";
    private static String privateKey = "dfbf32c6cd4cbbb69d4a6d8c547636eaa4ba9fe28db3dec1272f03755111f7d7";

    @Before
    public void init(){
        ledgerYiService = new LedgerYiService();
        ledgerYiService.init();
        erc20ContractService = new Erc20ContractServiceImpl(ledgerYiService);
    }

    @Test
    public void deployErc20Contract(){
        TokenInfo tokenInfo = new TokenInfo("ERC20Basic", "BSC", 10000, 10);
        DeployContractReturn deployResult = erc20ContractService.deployContract(ownerAddress, privateKey, tokenInfo);
        System.out.println("contract bytecode: " + deployResult.getContractByteCodes());
        System.out.println("contract address: " + deployResult.getContractAddress());
    }

    //private String contractAddress = "02a7109c8c5a28aa705960f9786eef28085805b2";
    private String contractAddress = "9e31bda5a03b9a60973ab1d111c7aa5d38982300";

    @Test
    public void getContractFromOnChain(){
        String abi = ledgerYiService.getContractFromOnChain(contractAddress);
        System.out.println(abi);
    }

    @Test
    public void tokenInfo() {
        TokenInfo tokenInfo = erc20ContractService.getTokenInfo(ownerAddress, contractAddress, privateKey);
        System.out.println("token info: " + tokenInfo.toString());
    }

    @Test
    public void balanceOf() {
        String owner = "bb8d66327f7bf0e91e7b7c118aebeca4f730de64";
        long result = erc20ContractService.balanceOf(owner, contractAddress, privateKey);
        System.out.println("trigger contract result: " + result);
    }

    @Test
    public void transfer() {
        String receiver = "bb8d66327f7bf0e91e7b7c118aebeca4f730de64";
        erc20ContractService.transfer(ownerAddress,receiver,contractAddress,privateKey,500);
    }
}
