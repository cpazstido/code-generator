package ${targetPackage};

import org.springframework.stereotype.Service;
import cn.sofmit.base.service.imp.BaseService;
import ${tableClass.fullClassName};
import ${servicePackage}.I${tableClass.shortClassName}Service;


/**
 * ${tableClass.shortClassName}服务接口实现类
 * @author cn.sofmit
 *
 */
@Service
public class ${tableClass.shortClassName}ServiceImpl extends BaseService<${tableClass.shortClassName}> implements I${tableClass.shortClassName}Service{
}

