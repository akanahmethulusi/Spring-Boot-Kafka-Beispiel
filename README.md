===Spring Boot Kafka===


Beispielbeschreibung:

In diesem Beispiel wurde mithilfe von Kafka ein Topic erstellt, das als Queue fungiert.

<img width="416" height="267" alt="image" src="https://github.com/user-attachments/assets/f1abb500-920d-4719-b8a8-68c1e7a95901" />

<img width="645" height="266" alt="Bildschirmfoto 2025-12-18 um 09 04 07" src="https://github.com/user-attachments/assets/21c1566d-7f84-4807-bd29-54be392b3ed1" />



===========================================

Ablauf des Programms

Das Programm empfängt eine POST-Request von einem Client.
In diesem Fall wurde die Anfrage mit Postman getestet.

<img width="292" height="233" alt="image" src="https://github.com/user-attachments/assets/a5bc72d4-5a67-461d-af8a-eaae5ac22734" />
<img width="460" height="274" alt="Bildschirmfoto 2025-12-18 um 08 56 58" src="https://github.com/user-attachments/assets/8c397374-9c23-4e81-a63b-3b9f4794784a" />



===========================================

Start der Anwendung

Zunächst muss Docker Compose ausgeführt werden, um alle benötigten Container zu starten.

<img width="572" height="43" alt="Bildschirmfoto 2025-12-18 um 09 11 53" src="https://github.com/user-attachments/assets/b16d9cbf-175d-43ed-aef4-0b27081acab5" />



===========================================

IDs im Programm

In diesem Programm werden zwei IDs verwendet:


1. ID im StreamingController

Eine ID wird im StreamingController erzeugt und als Key für die Kafka-Nachricht verwendet.

<img width="573" height="43" alt="Bildschirmfoto 2025-12-18 um 09 11 53" src="https://github.com/user-attachments/assets/41b15948-ee26-4b5a-9729-26ab050306a7" />


2. ID im DTO (KMessage)

Die zweite ID befindet sich im DTO KMessage und ist Teil der Message (Payload).

<img width="517" height="146" alt="Bildschirmfoto 2025-12-18 um 12 36 12" src="https://github.com/user-attachments/assets/3d043dc0-090d-4be7-913b-1f38ad458807" />
<img width="372" height="510" alt="image" src="https://github.com/user-attachments/assets/1f57ee77-b3f3-4602-922b-e978db6be0aa" />
<img width="532" height="142" alt="image" src="https://github.com/user-attachments/assets/42db015d-40de-40a3-be37-04f9a9aadfd3" />



===========================================

Kafka-Cluster-Daten

Nachdem das Topic erstellt wurde, sind im Kafka-Cluster folgende Informationen sichtbar:

*  Partition
*  Offset
*  Key
*  Message
*  Timestamp

Der Key enthält eine ID, die vom StreamingController gesetzt wird.
Die Message stammt aus dem DTO KMessage.


===============================================================================

Docker-compose Vorbereitung

Docker-Image erstellen

Zunächst sollte ein Docker-Image erstellt werden.
In diesem Projekt wurde ein Spotify Kafka Docker-Image verwendet, da dieses Kafka und Zookeeper bereits gemeinsam konfiguriert bereitstellt.

Zookeeper ist dafür verantwortlich, die Synchronisierung zwischen Kafka-Instanzen sicherzustellen.
Kafka selbst dient als Nachrichten-Warteschlange (Message Queue).
Eine der wichtigsten Funktionen von Kafka ist es, große Datenströme zu verarbeiten, indem sie in Topics und Partitionen aufgeteilt und effizient betrieben werden.

Kafka läuft standardmäßig auf Port 9092

Zookeeper läuft auf Port 2181

Dieses Image wurde gewählt, um nur ein einziges Docker-Image zu verwenden.
Andernfalls müssten separate Images für Kafka und Zookeeper gestartet werden.
Das Spotify Kafka Image eignet sich gut für Test- und Entwicklungszwecke, sollte jedoch nicht in Produktionsumgebungen eingesetzt werden.


===========================================

Docker-Image-Erstellung

In diesem Fall ist es wichtig, dass ein Bash-Skript ausgeführt wird.
Dieses Skript ruft das Kafka-Tool kafka-topics.sh auf, um ein Topic zu erstellen.

Im Skript wurden folgende Werte gesetzt:

Replication Factor: 1

Partitionen: 1


===========================================

Erklärung der Konfiguration


Replication

Replication bedeutet, dass eine Nachricht innerhalb eines Topics repliziert wird.
Falls eine Kafka-Instanz ausfällt, kann eine andere Replik verwendet werden, sofern der Replication Factor größer als 1 ist.

Partitionen

Wenn mehrere Consumer parallel Nachrichten lesen sollen, sollte die Anzahl der Partitionen größer als 1 sein.

Beispiel-Kommando

command: >
  bash -c
  "(sleep 15s &&                                   # Nach dem Start der App wird das topics.sh-Skript mit dem create-Befehl ausgeführt
  /opt/kafka_2_11-0.10.1.0/bin/kafka-topics.sh
  --create
  --zookeeper localhost:2181
  --replication-factor 1
  --partitions 1                                   # Anzahl der Replikationen und Partitionen
  --topic kan-topic &) && (supervisord -n)"        # Erstellt das Topic mit dem Namen 'kan-topic


