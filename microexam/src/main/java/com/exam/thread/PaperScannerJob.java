package com.exam.thread;

import com.exam.dao.PaperDao;
import com.exam.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by deng on 2017/12/4.
 * 用于自动扫描可被批改的试卷并完成批改，每分钟扫描一次
 */
@Component
public class PaperScannerJob {
    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperDao paperDao;

    @Scheduled(cron="0 0/1 * * * ?")
    public void checkPaper() {
        List<Integer> checkablePaperIds = paperDao.getCheckablePaperIds();
        for (int paperId : checkablePaperIds) {
            paperService.countScoreForAPaper(paperId);
        }
    }
}
