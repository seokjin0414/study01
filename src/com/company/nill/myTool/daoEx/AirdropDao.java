package com.company.nill.myTool.daoEx;

import com.metavegas.admin.vo.table.TnEventAirdrop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AirdropDao {

    //  TN_EVENT_AIRDROP 리스트
    List<TnEventAirdrop> tnEventAirdropList(TnEventAirdrop tnEventAirdrop);

    //  TN_EVENT_AIRDROP 리스트 CNT
    int tnEventAirdropListCnt(TnEventAirdrop tnEventAirdrop);

    //  TN_EVENT_AIRDROP 등록
    int insertTnEventAirdrop(TnEventAirdrop tnEventAirdrop);

    //  TN_EVENT_AIRDROP 수정
    int updateTnEventAirdrop(TnEventAirdrop tnEventAirdrop);

}
