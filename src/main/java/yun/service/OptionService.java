package yun.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;
import yun.dao.OptionDao;
import yun.dto.OptionDto;
import yun.models.Option;

import java.util.Date;

@Singleton
public class OptionService extends GenericService<Option>{
    @Inject
    OptionDao optionDao;

    @UnitOfWork
    public Option getByName(String name){
        return optionDao.getByName(name);
    }

    /**
     * 修改
     * @param option
     * @return
     */
    @Transactional
    public CommonSdo updateOption(Option option,OptionDto dto) {
        option.setValue(dto.getValue());
        option.setModified(new Date());
        optionDao.update(option);
        return CommonSdo.success(option,"修改成功");
    }
}
