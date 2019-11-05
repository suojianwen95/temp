package yun.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import yun.dao.LevelDao;
import yun.dao.MemberDao;
import yun.dao.ResetCodeDao;
import yun.dto.MemberDto;
import yun.models.Level;
import yun.models.Member;
import yun.models.ResetCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Singleton
public class MemberService extends GenericService<Member> {
    @Inject
    MemberDao memberDao;

    @Inject
    LevelDao levelDao;

    @Inject
    ResetCodeDao resetCodeDao;

    @UnitOfWork
    public Member getByUsername(String username){
            return memberDao.getByUsername(username);
    }

    @UnitOfWork
    public Member getByEmail(String username){
        return memberDao.getByEmail(username);
    }

    @UnitOfWork
    public Member getByMobile(String username){
        return memberDao.getByMobile(username);
    }

    @UnitOfWork
    public Member getManager(){
        return memberDao.getManage();
    }

    @UnitOfWork
    public List<Member> getHr(){
        return memberDao.getHr();
    }

    /**
     * resetCode的有效期为1小时
     * @param member
     * @return
     */
    @Transactional
    public ResetCode resetCode(Member member){
        ResetCode resetCode = resetCodeDao.get(member);
        if(null!=resetCode){
            return resetCode;
        }else{
            resetCode = new ResetCode();
        }
        resetCode.setMember(member);

        String code = null;
        while (code==null){
            code = generateRandomCode();
            if(null!=resetCodeDao.get(code)){
                code = null;
            }
        }
        resetCode.setCode(code);
        resetCodeDao.create(resetCode);
        return resetCode;
    }

    protected String generateRandomCode(){
        return String.valueOf(new RandomDataGenerator().nextInt(100000,999999));
    }

    @UnitOfWork
    public ResetCode getResetCode(Long id){
        ResetCode resetCode = resetCodeDao.find(id);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-1);
        Date date = calendar.getTime();
        if(resetCode.getCreated().after(date)){
            return resetCode;
        }else{
            return null;
        }
    }

    @Transactional
    public ResetCode cleanResetCode(ResetCode resetCode){
        resetCode.setStatus(false);
        resetCodeDao.update(resetCode);
        return resetCode;
    }

    @Transactional
    public ResetCode validResetCode(ResetCode resetCode){
        resetCode.setValid(true);
        resetCodeDao.update(resetCode);
        return resetCode;
    }

    @Transactional
    public ResetCode processResetCode(ResetCode resetCode){
        resetCode.setProcessed(true);
        resetCode.setToken(String.valueOf(RandomUtils.nextInt())+resetCode.getCode());
        resetCodeDao.update(resetCode);
        return resetCode;
    }


    /**
     * 添加员工
     * @param dto
     * @return
     */
    @Transactional
    public CommonSdo<Member> createMember(MemberDto dto){
        if (null !=memberDao.getByUsername(dto.getUsername())){
            return CommonSdo.error("用户名已注册");
        }
        if (null !=memberDao.getByEmail(dto.getEmail())){
            return CommonSdo.error("邮箱已经注册");
        }
        if (null !=memberDao.getByMobile(dto.getMobile())){
            return CommonSdo.error("手机已经注册");
        }
        if(Strings.isNullOrEmpty(dto.getPassword())||dto.getPassword().length()<7){
            return CommonSdo.error("密码不能小于6位");
        }

        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setNickname(dto.getNickname());
        member.setPlainPassword(dto.getPassword());
        member.setEmail(dto.getEmail());
        member.setMobile(dto.getMobile());
        member.addLevel(levelDao.getByUid(Level.UID_STAFF));

        memberDao.create(member);
        return CommonSdo.success(member,"创建成功");
    }

    /**
     * 修改
     * @param member
     * @param dto
     * @return
     */
    @Transactional
    public CommonSdo updateMember(Member member, MemberDto dto){
        if (!member.getUsername().equals(dto.getUsername())
                &&null !=memberDao.getByUsername(dto.getUsername())){
            return CommonSdo.error("用户名已注册");
        }
        if (!member.getEmail().equals(dto.getEmail())
                &&null !=memberDao.getByEmail(dto.getEmail())){
            return CommonSdo.error("邮箱已经注册");
        }
        if (!Strings.isNullOrEmpty(dto.getMobile())
                &&!member.getMobile().equals(dto.getMobile())
                &&null !=memberDao.getByMobile(dto.getMobile())){
            return CommonSdo.error("手机已经注册");
        }
        if(!Strings.isNullOrEmpty(dto.getPassword())){
            if(dto.getPassword().length()<7){
                return CommonSdo.error("密码不能小于6位");
            }
            member.setPlainPassword(dto.getPassword());
        }

        member.setUsername(dto.getUsername());
        member.setNickname(dto.getNickname());
        member.setPlainPassword(dto.getPassword());
        member.setEmail(dto.getEmail());
        member.setMobile(dto.getMobile());
        memberDao.update(member);
        return CommonSdo.success(member,"修改成功");
    }


}
