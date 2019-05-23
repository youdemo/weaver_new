package gvo.util;

public class Snippet {
	select infono,(select cus_name from uf_kskx_customer where id=a.cus_name) as cus_name from uf_kskx_cus_product a order by id desc
}

