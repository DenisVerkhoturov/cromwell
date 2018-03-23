package cromwell.languages

import common.Checked
import common.transforms.CheckedAtoB
import common.validation.Parse.Parse
import cromwell.core.{WorkflowId, WorkflowOptions, WorkflowSourceFilesCollection}
import cromwell.languages.LanguageFactory.ImportResolver
import wom.core._
import wom.executable.WomBundle
import wom.expression.IoFunctionSet

trait LanguageFactory {
  def getWomBundle(workflowSource: WorkflowSource,
                   workflowOptionsJson: WorkflowOptionsJson,
                   importResolvers: List[ImportResolver],
                   languageFactories: List[LanguageFactory]): Checked[WomBundle]

  def createExecutable(womBundle: WomBundle,
                       inputs: WorkflowJson,
                       ioFunctions: IoFunctionSet): Checked[ValidatedWomNamespace]

  def validateNamespace(source: WorkflowSourceFilesCollection,
                        workflowOptions: WorkflowOptions,
                        importLocalFilesystem: Boolean,
                        workflowIdForLogging: WorkflowId,
                        ioFunctions: IoFunctionSet): Parse[ValidatedWomNamespace]
}

object LanguageFactory {
  type ImportResolver = CheckedAtoB[String, WorkflowSource]
}
