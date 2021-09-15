<!-- PROJECT LOGO -->
<br />
<p align="center">
    <img src="docs/app/logo.png" alt="Logo" width="120" height="120">

<h3 align="center">Pay My Buddy</h3>

  <p align="center">
    An online service where you can make money transfers with your entourage!
    <br />
    <br />
    <a href="#summary"><strong>↓ Explore the docs ↓</strong></a>
    <br />
    <br />
    <a href="https://github.com/d2680807/openclassrooms_6th-project_money-transfer-system/issues">Report Bug</a>
    ·
    <a href="https://github.com/d2680807/openclassrooms_6th-project_money-transfer-system/issues">Report Feature</a>
    ·
    <a href="mailto:rocambin@gmail.com">Contact Me</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
<summary id="summary">Table of Contents</summary>
  <ol>
    <li>
        <a href="#about-the-project">About The Project</a>
        <ul>
            <li><a href="#documentation">Concept Models</a></li>
            <li><a href="#prerequisites">Prerequisites</a></li>        
        </ul>
    </li>
    <li>
      <a href="#documentation">Documentation</a>
      <ul>
        <li><a href="#documentation">Business Domain Model</a></li>
        <li><a href="#documentation">Physical Data Model</a></li>
      </ul>
    </li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

<p id="about-the-project"></p>

*Pay My Buddy* provides an online account where you can send money to your entourage or get paid by them. When you want to
send money with *Pay My Buddy* it feels like the money is going straight into the recipient’s account ! 🎉

### Concept Models

#### Login Page

[![Maquette 001][model-001]](#)

#### Dashboard

[![Maquette 002][model-002]](#)

<p id="prerequisites"></p>

### Prerequisites

#### Java Development Kit (JDK)

- **Version:** 11.0.12

#### MySQL

- **Version:** 8.0.25
- **Port (localhost):** 3306

> If different port, replace it by your port in:
> 
> * `src/main/resourcesMapplication.properties`

#### Integrated Development Environment (IDE)

> Don't forget to add your  Data Source

- Maven installation required for commands:`mvn site`/`mvn test`

<!-- DOCUMENTATION -->

## Documentation

<p id="documentation"></p>

### Business Domain Model

[![Business Domain Model][doc-001]](#)

### Physical Data Model

[![Physical Data Model][doc-002]](#)

<!-- ACKNOWLEDGEMENTS -->

## Acknowledgements

<p id="acknowledgements"></p>

* [Project Assignment](https://openclassrooms.com/fr/paths/191/projects/740/assignment)
* [Meeting Report](https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DAJava_P6/Compte+rendu+de+re%CC%81union.pdf)
* [Agile Board](https://trello.com/b/kaACWvrf/appli-de-transfert-dargent)

<!-- CONTACT -->

## Contact

<p id="contact"></p>

David - [Mail me here!](mailto:rocambin@gmail.com)

[» Project Link](https://github.com/d2680807/openclassrooms_6th-project_money-transfer-system)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[model-001]: docs/app/login-page.png

[model-002]: docs/app/home-page.png

[doc-001]: docs/data-model/1-functions.png

[doc-002]: docs/data-model/2-data.png
