package ledgeryi.app.erc721;

import cn.chainboard.ledgeryi.app.erc.TokenInfo;
import cn.chainboard.ledgeryi.app.erc.service.Erc721ContractServiceImpl;
import cn.chainboard.ledgeryi.app.erc.service.LedgerYiService;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import org.junit.Before;
import org.junit.Test;

public class ERC721Test {

    private Erc721ContractServiceImpl erc721ContractService;
    private LedgerYiService ledgerYiService;

    private static String ownerAddress = "3ee8cbc2fcafb3194ad16aafee35f8ed6f5d4828";
    private static String privateKey = "5a4f055e263cacd8726ebb3c4ab5ca5f8effc1bfb3f3ede95ba1658fb66de20b";

    @Before
    public void init(){
        ledgerYiService = new LedgerYiService();
        ledgerYiService.init();
        erc721ContractService = new Erc721ContractServiceImpl(ledgerYiService);
    }

    @Test
    public void deployErc721Contract(){
        TokenInfo tokenInfo = new TokenInfo("NFT721", "CryptoKitties", 1000, 0);
        DeployContractReturn deployResult = erc721ContractService.deployContract(ownerAddress, privateKey, tokenInfo);
        System.out.println("contract bytecode: " + deployResult.getContractByteCodes());
        System.out.println("contract address: " + deployResult.getContractAddress());
    }

    private String contractAddress = "1930506483a73b00f6255b40eaac9f993293cc48";

    @Test
    public void getContractFromOnChain(){
        String abi = ledgerYiService.getContractFromOnChain(contractAddress);
        System.out.println(abi);
    }

    @Test
    public void ownerOf(){
        String ownerOfToken = erc721ContractService.ownerOfToken(ownerAddress, contractAddress, privateKey, 4);
        System.out.println("token owner address" + ownerOfToken);
    }

    @Test
    public void transfer(){
        erc721ContractService.transfer(ownerAddress, contractAddress, contractAddress, privateKey,4);
    }

    @Test
    public void totalSupply(){
        long totalSupply = erc721ContractService.totalSupply(ownerAddress, contractAddress, privateKey);
        System.out.println("total mint: " + totalSupply);
    }

    @Test
    public void balanceOf() {
        long balanceOf = erc721ContractService.balanceOf(ownerAddress, contractAddress, privateKey);
        System.out.println("balance of owner: " + balanceOf);
    }

    @Test
    public void tokenByIndex(){
        long tokenByIndex = erc721ContractService.tokenByIndex(ownerAddress, contractAddress, privateKey, 8);
        System.out.println("token index: " + tokenByIndex);
    }

    @Test
    public void tokenInfo(){
        TokenInfo tokenInfo = erc721ContractService.getTokenInfo(ownerAddress, contractAddress, privateKey);
        System.out.println("token info: " + tokenInfo.toString());
    }

    @Test
    public void mintToken() {
        erc721ContractService.mint(ownerAddress,contractAddress,privateKey,3);
    }

    @Test
    public void burnToken() {
        erc721ContractService.burn(ownerAddress,contractAddress,privateKey,1);
    }
}
