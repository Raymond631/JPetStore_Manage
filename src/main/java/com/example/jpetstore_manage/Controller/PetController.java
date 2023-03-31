package com.example.jpetstore_manage.Controller;

import com.example.jpetstore_manage.Common.CommonResponse;
import com.example.jpetstore_manage.Common.JwtUtil;
import com.example.jpetstore_manage.POJO.DataObject.PetProductDO;
import com.example.jpetstore_manage.POJO.MapStruct.PetMapping;
import com.example.jpetstore_manage.POJO.ViewObject.PetDetailVO;
import com.example.jpetstore_manage.Service.PetService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author Raymond Li
 * @create 2023-03-20 16:31
 * @description 宠物数据接口，前端通过AJAX请求数据，页面跳转请调用AllPageController中的接口
 * 查询“我”上架的宠物列表
 * 查看某个宠物的详情
 * 下架宠物（修改库存为-1）
 * 上架新宠物
 * 修改宠物信息
 */
@Slf4j
@RestController
public class PetController {
    /**
     * 业务层接口
     */
    @Autowired
    private PetService petService;

    /**
     * 对象转换器
     */
    @Autowired
    private PetMapping petMapping;

    /**
     * 调用service层，查找供应商为“当前用户”的宠物列表(userMainDO代表当前用户)
     */
    @GetMapping("/pets")
    public CommonResponse getPetList(@CookieValue("token") String token) {
        int userId = (int) JwtUtil.resolveToken(token).get("userId");
        List<PetProductDO> petProductDOList = petService.getPetList(userId);
        return CommonResponse.success(petMapping.toPetListVOList(petProductDOList));
    }

    /**
     * 调用service层，根据productId查询宠物详情
     */
    @GetMapping("/pets/{productId}")
    public CommonResponse getPetDetail(@PathVariable("productId") int productId) {
        PetProductDO petProductDO = petService.getPetDetail(productId);
        return CommonResponse.success(petMapping.toPetDetailVO(petProductDO));
    }


    /**
     * 对象转换
     * 调用service新增宠物，供应商为当前用户（userMainDO）
     */
    @PostMapping("/pets")
    public CommonResponse newPet(@RequestBody PetDetailVO petDetailVO, @CookieValue("token") String token) {
        int userId = (int) JwtUtil.resolveToken(token).get("userId");
        return petService.newPet(petMapping.toPetProductDO(petDetailVO), userId);
    }

    /**
     * 下架
     */
    @DeleteMapping("/pets/{productId}")
    public CommonResponse remove(@PathVariable("productId") int productId, @CookieValue("token") String token) {
        int userId = (int) JwtUtil.resolveToken(token).get("userId");
        return petService.remove(productId, userId);
    }

    /**
     * 修改宠物信息（productId和ItemId不可修改）
     * 上传图片请调用ImageController中的接口
     */
    @PutMapping("/pets")
    public CommonResponse updatePet(@RequestBody PetDetailVO petDetailVO, @CookieValue("token") String token) {
        int userId = (int) JwtUtil.resolveToken(token).get("userId");
        PetProductDO petProductDO = petMapping.toPetProductDO(petDetailVO);
        return petService.updatePet(petProductDO, userId);
    }

    /**
     * 图片上传接口
     * 保存新图片，删除老图片
     *
     * @Return 新图片存储路径
     * TODO 开发中，有bug
     */
    @PostMapping("/image/upload")
    public CommonResponse uploadImageAjax(@RequestParam("productId") int productId, @RequestBody MultipartFile multipartFile) throws IOException {
        String filePath = "D:/jpetstoreImage/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 文件名称
        String realFileName = multipartFile.getOriginalFilename();
        // 文件处理
        String newFileName = UUID.randomUUID() + "-" + realFileName;
        String newFilePath = filePath + newFileName;
        // 新文件的路径
        multipartFile.transferTo(new File(newFilePath));
        // 删除老文件，更新数据库路径
        petService.updateImage(productId, newFileName);
        return CommonResponse.success("上传成功");
    }

    @GetMapping("/image/look/{imageName}")
    public void imageLook(@PathVariable("imageName") String imageName, HttpServletResponse response) {
        System.out.println("D:/jpetstoreImage/" + imageName);
        File file = new File("D:/jpetstoreImage/" + imageName);
        byte[] bytes = new byte[1024];
        try (OutputStream os = response.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
            while ((fis.read(bytes)) != -1) {
                os.write(bytes);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
