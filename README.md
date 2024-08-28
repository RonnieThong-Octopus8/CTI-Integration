*iAgent Custom CRM Training*
Overview
This project provides an integration solution between Novomind and CiviCRM, offering a comprehensive Computer Telephony Integration (CTI) system. The project is structured with multiple plugins, providing core functionalities such as chat handling, agent routing, and CRM integration.

The solution is designed with flexibility in mind, offering fully dynamic API configurations to different CRM systems.

Features
Modular Design: Multiple plugins, each handling specific parts of the CRM and contact center integrations.
CTI Capabilities: Seamless integration between CRM systems and telephony, allowing for automated call routing, agent management, and customer handling.
Dynamic Configuration: API keys and endpoints can be dynamically configured for different CRM systems, enabling flexibility across multiple environments.
Project Structure
ecom-app-iagent-custom-crm-training-agent-plugin: Handles agent-related functionalities.
ecom-app-iagent-custom-crm-training-chat-plugin: Manages chat interactions.
ecom-app-iagent-custom-crm-training-core-plugin: Core CRM functionalities and integrations.
ecom-app-iagent-custom-crm-training-routing-plugin: Handles routing logic for calls and chats.
gradle/: Gradle wrapper files for building and managing dependencies.
src/: Main source code for the core application and plugins.
Installation Guide
Prerequisites
Java 11+: Ensure that you have Java 11 or higher installed on your system. Download here.
Gradle: The project uses Gradle as its build tool. Ensure Gradle is installed or use the provided Gradle wrapper (gradlew).
Git: Make sure Git is installed. Download here.
CiviCRM & Novomind API Credentials: Obtain necessary API keys for connecting to both CiviCRM and Novomind.
Installation
Clone the Repository:

bash
Copy code
git clone https://github.com/yourusername/novomind-applications.git
cd novomind-applications/iagent-civicrm-master
Configure API Keys:

Navigate to the appropriate configuration files in the config/ directory.
Replace placeholders with your actual CiviCRM and Novomind API credentials.
Build the Project: Use Gradle to build the project:

bash
Copy code
./gradlew clean build
Run the Application: You can run the application directly from the Gradle command line:

bash
Copy code
./gradlew run
Updating the Project
Pull the Latest Changes:

bash
Copy code
git pull origin main
Rebuild the Project: After pulling updates, rebuild the project to apply the latest changes:

bash
Copy code
./gradlew clean build
Run the Updated Application: Execute the application as usual:

bash
Copy code
./gradlew run
Troubleshooting
Dependency Issues: Run the following command if you encounter any issues with missing dependencies:

bash
Copy code
./gradlew dependencies
Configuration Errors: Ensure that all API keys and environment variables are properly configured and formatted in the appropriate files.

Contributing
If you'd like to contribute, fork the repository, make your changes, and submit a pull request. Contributions to improve the modularity, performance, or documentation are welcome.

License
This project is licensed under the MIT License. See the LICENSE file for more details.

Contact
For more information or support, please contact:

Ronnie Thong
ronniethong@octopus8.com
