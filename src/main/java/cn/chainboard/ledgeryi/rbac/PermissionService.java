package cn.chainboard.ledgeryi.rbac;

import cn.chainboard.ledgeryi.app.erc.service.LedgerYiService;
import cn.ledgeryi.sdk.common.utils.DecodeUtil;
import cn.ledgeryi.sdk.contract.compiler.exception.ContractException;
import cn.ledgeryi.sdk.exception.CreateContractExecption;
import cn.ledgeryi.sdk.serverapi.data.DeployContractParam;
import cn.ledgeryi.sdk.serverapi.data.DeployContractReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author Brian
 * @date 2021/3/10 19:56
 */
@Slf4j
@Service
public class PermissionService {

    @Resource
    private LedgerYiService ledgerYiService;

    public void addNewRole(RoleTypeEnum roleType, String callAddress,
                           String contractAddress, String privateKey) {
        String method = "addRole(uint32)";
        List<Object> args = Arrays.asList(roleType.getType());
        ledgerYiService.callContract(callAddress, contractAddress, method, args, privateKey);
    }

    public void addAccount(String address, RoleTypeEnum roleType, String callAddress,
                           String contractAddress, String privateKey) {
        String method = "addAccount(address,uint32)";
        List<Object> args = Arrays.asList(address, roleType.getType());
        ledgerYiService.callContract(callAddress, contractAddress, method, args, privateKey);
    }

    public void addNode(NewNode newNode, String callAddress, String contractAddress, String privateKey) {
        String method = "addNode(address,string,uint32)";
        List<Object> args = Arrays.asList(newNode.getOwner(),newNode.getHost(),newNode.getPort());
        ledgerYiService.callContract(callAddress, contractAddress, method, args, privateKey);
    }

    public String deployContract(String ownerAddress, String privateKey, String contractName){
        DeployContractParam param;
        try {
            Path source = Paths.get("contract","rbac", contractName + ".sol");
            param = ledgerYiService.compileContractFromFile(source,contractName);
        } catch (ContractException e) {
            log.error("compile contract error: " + e.getMessage());
            return null;
        }
        DeployContractReturn deployContract;
        try {
            deployContract = ledgerYiService.deployContract(DecodeUtil.decode(ownerAddress),
                    DecodeUtil.decode(privateKey), param);
            return deployContract.getContractAddress();
        } catch (CreateContractExecption e) {
            log.error("create contract error: " + e.getMessage());
            return null;
        }
    }
}