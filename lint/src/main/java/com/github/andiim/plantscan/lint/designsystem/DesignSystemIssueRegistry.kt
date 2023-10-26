package com.github.andiim.plantscan.lint.designsystem

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class DesignSystemIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(DesignSystemDetector.ISSUE)

    override val api: Int = CURRENT_API
    override val minApi: Int= 12
    override val vendor: Vendor = Vendor(
        vendorName = "PlantScan",
        feedbackUrl = "https://github.com/Andi-IM/PlantScan/issues",
        contact = "https://github.com/Andi-IM/PlantScan"
    )
}