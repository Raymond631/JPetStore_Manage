package com.example.jpetstore_manage.POJO.MapStruct;

import com.example.jpetstore_manage.POJO.DataObject.PetProductDO;
import com.example.jpetstore_manage.POJO.ViewObject.PetDetailVO;
import com.example.jpetstore_manage.POJO.ViewObject.PetListVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-03-21 19:55
 * @description
 */
@Mapper(componentModel = "spring")
public interface PetMapping {

    public List<PetListVO> toPetListVOList(List<PetProductDO> petProductDOList);


    public PetDetailVO toPetDetailVO(PetProductDO petProductDO);

    public PetProductDO toPetProductDO(PetDetailVO petDetailVO);
}
