# Interface_idempotency
接口幂等性问题


##Idempotency 注解


##
主要采用的是将请求的用户身份信息、请求URL+请求参数，做一个MD5的加密产生一个秘钥字符串。将字符串以redis的setnx进行存储，这样在重复提交的时候只要设置这个key就会返回失败，直接报错异常。
