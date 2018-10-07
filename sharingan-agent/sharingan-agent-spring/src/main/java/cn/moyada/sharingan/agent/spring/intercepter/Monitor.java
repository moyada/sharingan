package cn.moyada.sharingan.agent.spring.intercepter;

import cn.moyada.sharingan.monitor.api.Protocol;

public @interface Monitor {

    Protocol[] value();
}
