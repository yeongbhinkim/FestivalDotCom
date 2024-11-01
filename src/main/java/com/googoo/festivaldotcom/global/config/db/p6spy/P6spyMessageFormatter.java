package com.googoo.festivaldotcom.global.config.db.p6spy;


import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Locale;

// 이 클래스는 'default' 프로필에서 활성화되며 Spring의 Configuration으로 등록됩니다.
@Profile("default")
@Configuration
public class P6spyMessageFormatter implements MessageFormattingStrategy {

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category,
								String prepared, String sql, String url) {

		// SQL을 포맷하고 카테고리와 실행 시간을 포함한 메시지를 생성합니다.
		sql = formatSql(category, sql);
//		return category + " | " + "OperationTime : " + elapsed + "ms" + sql;

		String methodName = getCallingMethodName(); // 호출된 메소드명을 가져옴
		return category + " | " + "OperationTime : " + elapsed + "ms | Method: " + methodName + " | " + sql;
	}

	// SQL 쿼리를 특정 형식에 맞게 포맷합니다.
	private String formatSql(String category, String sql) {
		if (sql == null || sql.isBlank()) {
			return sql;
		}

		// SQL 카테고리가 'STATEMENT'인 경우, SQL 문을 특정 형식에 따라 포맷합니다.
		if (Category.STATEMENT.getName().equals(category)) {
			String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
			if (tmpsql.startsWith("create") || tmpsql.startsWith("alter")) {
				sql = FormatStyle.DDL.getFormatter().format(sql);
			} else {
				sql = FormatStyle.BASIC.getFormatter().format(sql);
			}
		}

		sql += "\n";

		// 스택 트레이스 정보를 SQL 로그에 추가합니다.
		String[] stackTrace = stackTrace();

		if (stackTrace.length > 0) {
			sql += Arrays.toString(stackTrace).replace(", ", "\n");
		}

		return sql;
	}

	// 스택 트레이스를 반환하는 메소드입니다. 특정 클래스 경로를 포함하고 일부 경로를 제외합니다.
	private String[] stackTrace() {
		return Arrays.stream(new Throwable().getStackTrace())
				.map(StackTraceElement::toString)
				.filter(string -> string.startsWith("com.googoo.festivaldotcom")
						&& !string.startsWith("com.googoo.festivaldotcom.global.config.db.p6spy")
						&& !string.startsWith("com.googoo.festivaldotcom.FestivalDotComApplication.main"))
				.toArray(String[]::new);
	}

	// 호출된 메소드명을 반환하는 메소드입니다.
	private String getCallingMethodName() {
		return Arrays.stream(new Throwable().getStackTrace())
				.filter(stackTraceElement -> stackTraceElement.getClassName().startsWith("com.googoo.festivaldotcom")
						&& !stackTraceElement.getClassName().startsWith("com.googoo.festivaldotcom.global.config.db.p6spy"))
				.findFirst()
				.map(StackTraceElement::getMethodName)
				.orElse("UnknownMethod");
	}

}