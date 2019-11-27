package cn.xiaochi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.Set;

/**
 * 发送邮件工具
 */
@Slf4j
public class MailUtil {

    /**
     * 发送邮件
     * @param subject  邮件主题
     * @param message 邮件信息（可以是 html）
     * @param receivers 收件人（可以是多个）
     * @return
     */
    public static boolean send(String subject, String message, Set<String> receivers) {

        // TODO
        String from = "";
        int port = 25;
        String host = "";
        String pass = "";
        String nickname = "";

        HtmlEmail email = new HtmlEmail();
        try {
            email.setHostName(host);
            email.setCharset("UTF-8");
            for (String str : receivers) {
                email.addTo(str);
            }
            email.setFrom(from, nickname);
            email.setSmtpPort(port);
            email.setAuthentication(from, pass);
            email.setSubject(subject);
            email.setMsg(message);
            email.send();
            log.info("{} 发送邮件到 {}", from, StringUtils.join(receivers, ","));
            return true;
        } catch (EmailException e) {
            log.error(from + "发送邮件到" + StringUtils.join(receivers, ",") + "失败", e);
            return false;
        }
    }

}

