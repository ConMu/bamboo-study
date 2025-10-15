package com.conmu.sms.controller;

import com.conmu.sms.dao.entity.People;
import com.conmu.sms.dao.entity.User;
import com.conmu.sms.service.PeopleService;
import com.conmu.sms.service.UserService;
import com.conmu.sms.util.ApiResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 多数据源测试Controller
 * @author mucongcong
 * @date 2025/10/14 17:48
 * @since
 **/
@Api(tags = "多数据源测试接口")
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PeopleService peopleService;

    // ==================== 用户管理 (数据源1) ====================

    @ApiOperation(value = "创建用户", notes = "在指定数据源中创建用户信息 (可使用dsNo=ds0或ds1)")
    @ApiResponses({
        @ApiResponse(code = 200, message = "创建成功"),
        @ApiResponse(code = 500, message = "创建失败")
    })
    @PostMapping("/user")
    public ApiResult<User> createUser(
            @ApiParam(name = "user", value = "用户信息", required = true)
            @RequestBody User user) {
        try {
            // 数据预处理 - 确保phone字段是字符串
            if (user.getPhone() != null) {
                user.setPhone(String.valueOf(user.getPhone()));
            }

            // 设置默认值
            if (user.getCreatedBy() == null) {
                user.setCreatedBy(1L);
            }
            if (user.getUpdatedBy() == null) {
                user.setUpdatedBy(1L);
            }
            if (user.getDeleted() == null) {
                user.setDeleted(0);
            }

            int result = userService.insert(user);
            if (result > 0) {
                return ApiResult.success("用户创建成功", user);
            } else {
                return ApiResult.error("用户创建失败");
            }
        } catch (Exception e) {
            return ApiResult.error("用户创建异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户名查询用户", notes = "查询用户信息 (可使用dsNo=ds0或ds1)")
    @GetMapping("/user/{username}")
    public ApiResult<User> getUser(
            @ApiParam(name = "username", value = "用户名", required = true, example = "admin")
            @PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                return ApiResult.success(user);
            } else {
                return ApiResult.error("用户不存在");
            }
        } catch (Exception e) {
            return ApiResult.error("查询用户异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "更新用户信息", notes = "更新数据源1中的用户信息")
    @PutMapping("/user")
    public ApiResult<String> updateUser(
            @ApiParam(name = "user", value = "用户信息", required = true)
            @RequestBody User user) {
        try {
            int result = userService.update(user);
            if (result > 0) {
                return ApiResult.success("用户更新成功");
            } else {
                return ApiResult.error("用户更新失败");
            }
        } catch (Exception e) {
            return ApiResult.error("用户更新异常: " + e.getMessage());
        }
    }

    // ==================== 人员管理 (数据源2) ====================

    @ApiOperation(value = "创建人员", notes = "在指定数据源中创建人员信息 (可使用dsNo=ds0或ds1)")
    @ApiResponses({
        @ApiResponse(code = 200, message = "创建成功"),
        @ApiResponse(code = 500, message = "创建失败")
    })
    @PostMapping("/people")
    public ApiResult<People> createPeople(
            @ApiParam(name = "people", value = "人员信息", required = true)
            @RequestBody People people) {
        try {
            // 数据预处理 - 确保phone字段是字符串
            if (people.getPhone() != null) {
                people.setPhone(String.valueOf(people.getPhone()));
            }

            // 设置默认值
            if (people.getCreatedBy() == null) {
                people.setCreatedBy(1L);
            }
            if (people.getUpdatedBy() == null) {
                people.setUpdatedBy(1L);
            }
            if (people.getDeleted() == null) {
                people.setDeleted(0);
            }

            int result = peopleService.insert(people);
            if (result > 0) {
                return ApiResult.success("人员创建成功", people);
            } else {
                return ApiResult.error("人员创建失败");
            }
        } catch (Exception e) {
            return ApiResult.error("人员创建异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "根据姓名查询人员", notes = "查询人员信息 (可使用dsNo=ds0或ds1)")
    @GetMapping("/people/{name}")
    public ApiResult<People> getPeople(
            @ApiParam(name = "name", value = "姓名", required = true, example = "张三")
            @PathVariable String name) {
        try {
            People people = peopleService.findByName(name);
            if (people != null) {
                return ApiResult.success(people);
            } else {
                return ApiResult.error("人员不存在");
            }
        } catch (Exception e) {
            return ApiResult.error("查询人员异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询人员", notes = "根据ID查询人员信息 (可使用dsNo=ds0或ds1)")
    @GetMapping("/people/id/{id}")
    public ApiResult<People> getPeopleById(
            @ApiParam(name = "id", value = "人员ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            People people = peopleService.findById(id);
            if (people != null) {
                return ApiResult.success(people);
            } else {
                return ApiResult.error("人员不存在");
            }
        } catch (Exception e) {
            return ApiResult.error("查询人员异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "更新人员信息", notes = "更新数据源2中的人员信息")
    @PutMapping("/people")
    public ApiResult<String> updatePeople(
            @ApiParam(name = "people", value = "人员信息", required = true)
            @RequestBody People people) {
        try {
            int result = peopleService.update(people);
            if (result > 0) {
                return ApiResult.success("人员更新成功");
            } else {
                return ApiResult.error("人员更新失败");
            }
        } catch (Exception e) {
            return ApiResult.error("人员更新异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "删除人员", notes = "从数据源2删除人员信息(逻辑删除)")
    @DeleteMapping("/people/{id}")
    public ApiResult<String> deletePeople(
            @ApiParam(name = "id", value = "人员ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            int result = peopleService.deleteById(id);
            if (result > 0) {
                return ApiResult.success("人员删除成功");
            } else {
                return ApiResult.error("人员删除失败");
            }
        } catch (Exception e) {
            return ApiResult.error("人员删除异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "测试接口", notes = "简单的测试接口，无需数据库连接")
    @GetMapping("/test")
    public ApiResult<String> test() {
        return ApiResult.success("API测试成功！Swagger集成正常工作。");
    }
}
