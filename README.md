# iAgent CTI Integration with CiviCRM

## Overview This project provides an integration solution between Novomind and CiviCRM, offering a comprehensive Computer Telephony Integration (CTI) system. The project is structured with multiple plugins, providing core functionalities such as chat handling, agent routing, and CRM integration.

The solution is designed with flexibility in mind, offering fully dynamic API configurations to different CRM systems.

## Features ### Modular Design Multiple plugins, each handling specific parts of the CRM and contact center integrations.

### CTI Capabilities Seamless integration between CRM systems and telephony, allowing for automated call routing, agent management, and customer handling.

### Dynamic Configuration API keys and endpoints can be dynamically configured for different CRM systems, enabling flexibility across multiple environments.

## Project Structure

ecom-app-iagent-custom-crm-training-agent-plugin: Handles agent-related functionalities.
ecom-app-iagent-custom-crm-training-chat-plugin: Manages chat interactions.
ecom-app-iagent-custom-crm-training-core-plugin: Core CRM functionalities and integrations.
ecom-app-iagent-custom-crm-training-routing-plugin: Handles routing logic for calls and chats.
gradle/: Gradle wrapper files for building and managing dependencies.
src/: Main source code for the core application and plugins.
## Installation Guide

### Prerequisites

**Java 11+**: Ensure that you have Java 11 or higher installed on your system. 
𝐷
𝑜
𝑤
𝑛
𝑙
𝑜
𝑎
𝑑
ℎ
𝑒
𝑟
𝑒
Downloadhere(https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
**Gradle**: The project uses Gradle as its build tool. Ensure Gradle is installed or use the provided Gradle wrapper (gradlew).
**Git**: Make sure Git is installed. 
𝐷
𝑜
𝑤
𝑛
𝑙
𝑜
𝑎
𝑑
ℎ
𝑒
𝑟
𝑒
Downloadhere(https://git-scm.com/downloads).
**CiviCRM & Novomind API Credentials**: Obtain necessary API keys for connecting to both CiviCRM and Novomind.
### Installation

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

arduino
Copy code
./gradlew run
## Running the Project

### Uploading to Novomind

To run the project, you need to upload it directly into the Novomind applications section. The steps are as follows:

Prepare the Project:

Ensure that the project is built and packaged correctly.
Verify that all necessary configurations are included in the project files.
Upload the Project:

Navigate to the Novomind applications section.
Upload the packaged project directly into this section.
### Configuring the Application

Once the project is uploaded:

Access Application Configurations:

Inside the Novomind applications section, locate the uploaded application.
Simply press on the application.
Configuration Details:

The configuration details for the application will automatically appear.
Adjust the settings as needed according to your environment or integration requirements.
## Updating the Project

Pull the Latest Changes from the Remote Repository: Fetch and merge the changes from the remote repository into your local branch:

css
Copy code
git pull origin main --rebase
Resolve Any Merge Conflicts (if any): If there are any merge conflicts, Git will notify you. You'll need to resolve the conflicts manually by editing the conflicting files. After resolving the conflicts:

csharp
Copy code
git add .
git rebase --continue
Push Your Changes: Once you've successfully pulled the latest changes and resolved any conflicts, push your changes to the remote repository:

css
Copy code
git push -u origin main
## Troubleshooting

**Dependency Issues**: Run the following command if you encounter any issues with missing dependencies:

bash
Copy code
./gradlew dependencies
**Configuration Errors**: Ensure that all API keys and environment variables are properly configured and formatted in the appropriate files.

## Contributing

If you'd like to contribute, fork the repository, make your changes, and submit a pull request. Contributions to improve the modularity, performance, or documentation are welcome.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

## Contact

For more information or support, please contact:

**Ronnie Thong**
𝑟
𝑜
𝑛
𝑛
𝑖
𝑒
𝑡
ℎ
𝑜
𝑛
𝑔
@
𝑜
𝑐
𝑡
𝑜
𝑝
𝑢
𝑠
8.
𝑐
𝑜
𝑚
ronniethong@octopus8.com(mailto
@octopus8.com)
