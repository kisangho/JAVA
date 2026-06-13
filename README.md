# 🎰 Java Swing Slot Machine

자바 스윙(Java Swing)을 활용하여 구현한 심플하고 직관적인 **데스크톱 슬롯머신 게임**입니다. 
스레드 기반의 타이머 애니메이션과 파일 입출력을 통한 데이터 보존 기능을 포함하고 있습니다.

---

## ✨ 주요 기능 (Key Features)

* **슬롯 애니메이션**: `javax.swing.Timer`를 활용하여 3개의 릴(Reel)이 순차적으로 멈추는 회전 효과를 구현했습니다.
* **코인 세이브 시스템**: 게임 종료 시 코인 잔액이 `coin.txt` 파일에 자동으로 저장되며, 재실행 시 기존 잔액을 그대로 불러옵니다. (기본 보유 코인: 500)
* **당첨 로직**: 3개의 슬롯 모양이 모두 일치할 경우(**7-7-7, A-A-A, B-B-B**) 베팅액의 2배를 획득합니다.

---

## 🛠️ 기술 스택 (Tech Stack)

* **Language**: Java 8+
* **Framework**: Java Swing / AWT

---

## 🚀 실행 방법 (How to Run)

1. 이 저장소를 클론하거나 소스 코드를 다운로드합니다.
2. `SlotMachineFrame.java` 파일을 컴파일하고 실행합니다.

```bash
javac SlotMachineFrame.java
java SlotMachineFrame
