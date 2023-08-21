package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionController extends BaseController {
    private final PermissionService permissionService;
    private final Logger logger= LoggerFactory.getLogger(PermissionController.class);
    @GetMapping("/{subId}")
    public ResponseEntity<Set<Permission>> getPermission(@PathVariable Long subId) {
        var user=getUserByToken();
        logger.info(MessageFormat.format("user with phone number:{0} wants to get permissions of sub:{1}",user.getPhoneNumber(),subId));
        return new ResponseEntity<>(permissionService.getPermissions(subId, user.getUserId()), HttpStatus.OK);
    }
    @PutMapping("/{subId}")
    public ResponseEntity<Set<Permission>> updatePermission(@PathVariable Long subId,@RequestBody Set<Permission> permissions) {
        var user=getUserByToken();
        logger.info(MessageFormat.format("user with phone number:{0} wants to update permissions of sub:{1}",user.getPhoneNumber(),subId));
        return new ResponseEntity<>(permissionService.updatePermissions(subId, user.getUserId(),permissions), HttpStatus.OK);
    }

}
