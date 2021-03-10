package cn.chainboard.ledgeryi.rbac;

import cn.ledgeryi.api.GrpcAPI.GrpcRequest;
import cn.ledgeryi.api.PermissionGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Brian
 * @date 2021/3/10 17:58
 */
public class PermissionGrpcClient {

//    private PermissionGrpc.PermissionBlockingStub permissionBlockingStub;
//
//    public void init(String ledgerYiNode){
//        if (!StringUtils.isEmpty(ledgerYiNode)) {
//            ManagedChannel channelFull = ManagedChannelBuilder.forTarget(ledgerYiNode).usePlaintext(true).build();
//            permissionBlockingStub = PermissionGrpc.newBlockingStub(channelFull);
//        }
//    }
//
//    public void addNewRole(RoleTypeEnum roleType){
//        GrpcRequest.Builder builder = GrpcRequest.newBuilder();
//        permissionBlockingStub.addNewRole()
//    }

}
