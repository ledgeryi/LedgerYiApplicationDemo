package cn.chainboard.ledgeryi.app.erc.service;

import cn.chainboard.ledgeryi.app.erc.TokenInfo;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import cn.ledgeryi.sdk.serverapi.data.TriggerContractReturn;

public interface IContractService {

    /**
     * 部署合约
     * @param ownerAddress 合约拥有者地址
     * @param privateKey 拥有者私钥
     * @param tokenInfo 合约参数
     * @return DeployContractReturn
     */
    DeployContractReturn deployContract(String ownerAddress, String privateKey, TokenInfo tokenInfo);

    /**
     * 查询账户余额
     * @param ownerAddress 查询者地址
     * @param contractAddress 合约地址
     * @param privateKey 查询者私钥
     * @return Long
     */
    long balanceOf(String ownerAddress, String contractAddress, String privateKey);

    /**
     * 查询发布总数
     * @param ownerAddress 查询者地址
     * @param contractAddress 合约地址
     * @param privateKey 查询者私钥
     * @return Long
     */
    long totalSupply(String ownerAddress, String contractAddress, String privateKey);

    /**
     * 销毁
     * @param ownerAddress token拥有者地址
     * @param contractAddress 合约地址
     * @param privateKey token拥有者私钥
     * @param num 销毁数量（erc20）， 或 销毁的tokenID（erc721）
     * @return TriggerContractReturn
     */
    TriggerContractReturn burn(String ownerAddress, String contractAddress, String privateKey, long num);

    /**
     * 增发
     * @param ownerAddress 合约拥有者地址
     * @param contractAddress 合约地址
     * @param privateKey 合约拥有者私钥
     * @param num 增发数量
     * @return TriggerContractReturn
     */
    TriggerContractReturn mint(String ownerAddress, String contractAddress, String privateKey, long num);

    /**
     * 转账
     * @param ownerAddress token拥有者地址
     * @param contractAddress 合约地址
     * @param privateKey token拥有者私钥
     * @param num 销毁数量（erc20）， 或 销毁的tokenID（erc721）
     * @param to 接收地址
     * @return TriggerContractReturn
     */
    TriggerContractReturn transfer(String ownerAddress, String to, String contractAddress, String privateKey, long num);

    /**
     * 查询合约发布的token信息
     * @param ownerAddress 查询者地址
     * @param contractAddress 合约地址
     * @param privateKey 查询者私钥
     * @return TokenInfo
     */
    TokenInfo getTokenInfo(String ownerAddress, String contractAddress, String privateKey);

    /**
     * ERC721合约，根据token id查询token的所有者
     * @param ownerAddress 查询者地址
     * @param contractAddress 合约地址
     * @param privateKey 查询者私钥
     * @param tokenId token ID
     * @return token的所有者地址
     */
    String ownerOfToken(String ownerAddress, String contractAddress, String privateKey, long tokenId);

    /**
     * ERC721合约，根据token index查询token
     * @param ownerAddress 查询者地址
     * @param contractAddress 合约地址
     * @param privateKey 查询者私钥
     * @param tokenIndex token index
     * @return token
     */
    long tokenByIndex(String ownerAddress, String contractAddress, String privateKey, long tokenIndex);

}
