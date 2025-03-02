package org.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient // 이 서비스는 유레카 클라이언트이다 <- eureka-server가 찾아서 등록하는 대상 서비스
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	/**
	 * 게이트웨이에서 각종 서비스를 등록하는 방법중 코드로 등록 -> 빈
	 * RouteLocator 객체를 빈으로 등록
	 */
	@Bean
	public RouteLocator ecomRouteLocator(RouteLocatorBuilder builder) {
		System.out.println("게스트웨이에서 개별 서비스 URL 등록");
		return builder.routes()
				// 개별 라우트 등록
				// 서비스별 URL 별칭이 1개인 경우, n개인 경우도 존재
				// ex) 회원업무 -> 회원관리(member|user|..), 인증(auth) 분리 => 각각 등록
				//.route("서비스별 appplication.yml에서 정의된 spring.application.name의 값",
				//        r->r.path("/인증/**").uri("lb://spring.application.name의값")  )
				// 등록예정 : 회원관리, 회원인증, 제품관리, 장바구니관리(api만 구성 현재생략기준),
				//           주문관리, 결제관리

				// 회원관리 -> url 프리픽스 2개 사용
                .route("msa-sb-user",
                        r->r.path("/auth/**").uri("lb://msa-sb-user")  )
                .route("msa-sb-user",
                        r->r.path("/user/**").uri("lb://msa-sb-user")  )
				.build();
	}

}