package womtool.cmdline

import common.util.VersionUtil
import cromwell.core.path.DefaultPathBuilder
import womtool.cmdline.HighlightMode.{ConsoleHighlighting, HtmlHighlighting, UnrecognizedHighlightingMode}
import womtool.cmdline.WomtoolCommand._
import womtool.cmdline.WomtoolCommandLineParser._

object WomtoolCommandLineParser {

  lazy val cromwellVersion = VersionUtil.getVersion("cromwell")

  lazy val instance: scopt.OptionParser[PartialWomtoolCommandLineArguments] = new WomtoolCommandLineParser()

  def validateCommandLine(args: PartialWomtoolCommandLineArguments): Option[ValidatedWomtoolCommandLine] = args match {
    case PartialWomtoolCommandLineArguments(Some(Validate), Some(mainFile), inputs, None) => Option(ValidateCommandLine(mainFile, inputs))
    case PartialWomtoolCommandLineArguments(Some(Inputs), Some(mainFile), None, None) => Option(InputsCommandLine(mainFile))
    case PartialWomtoolCommandLineArguments(Some(Parse), Some(mainFile), None, None) => Option(ParseCommandLine(mainFile))
    case PartialWomtoolCommandLineArguments(Some(Highlight), Some(mainFile), None, Some(mode)) => Option(HighlightCommandLine(mainFile, mode))
    case PartialWomtoolCommandLineArguments(Some(Graph), Some(mainFile), None, None) => Option(WomtoolGraphCommandLine(mainFile))
    case PartialWomtoolCommandLineArguments(Some(V1Upgrade), Some(mainFile), None, None) => Option(WomtoolWdlV1UpgradeCommandLine(mainFile))
    case PartialWomtoolCommandLineArguments(Some(WomGraph), Some(mainFile), None, None) => Option(WomtoolWomGraphCommandLine(mainFile))
    case _ => None
  }
}

class WomtoolCommandLineParser extends scopt.OptionParser[PartialWomtoolCommandLineArguments]("java -jar womtool.jar") {

  arg[String]("workflow-source")
    .text("Path to workflow file.")
    .required
    .action((s, c) => c.copy(workflowSource = Option(DefaultPathBuilder.get(s))))

  opt[String]('i', "inputs")
    .text("Workflow inputs file.")
    .optional
    .action((s, c) => c.copy(workflowInputs = Option(DefaultPathBuilder.get(s))))

  opt[String]('h', "highlight-mode")
    .text("Highlighting mode, one of 'html', 'console' (used only with 'highlight' command)")
    .optional
    .action((s, c) => s match {
      case "html" => c.copy(highlightMode = Option(HtmlHighlighting))
      case "console" => c.copy(highlightMode = Option(ConsoleHighlighting))
      case other => c.copy(highlightMode = Option(UnrecognizedHighlightingMode(other)))
    })

  head("womtool", cromwellVersion)

  help("help")

  version("version")

  // For 'usage' layout reasons:
  note("")

  cmd("validate")
    .action((_, c) => c.copy(command = Option(Validate)))
    .text("Validate a workflow source file. If inputs are provided then 'validate' also checks that the inputs file is a valid set of inputs for the workflow." + System.lineSeparator)

  cmd("inputs")
    .action((_, c) =>
      c.copy(command = Option(Inputs)))
    .text("Generate and output a new inputs JSON for this workflow." + System.lineSeparator)

  cmd("parse")
    .action((_, c) => c.copy(command = Option(Parse)))
    .text("(Deprecated; WDL draft 2 only) Print out the Hermes parser's abstract syntax tree for the source file." + System.lineSeparator)

  cmd("highlight")
    .action((_, c) => c.copy(command = Option(Highlight)))
    .text("(Deprecated; WDL draft 2 only) Print out the Hermes parser's abstract syntax tree for the source file. Requires at least one of 'html' or 'console'" + System.lineSeparator)

  cmd("graph")
    .action((_, c) => c.copy(command = Option(Graph)))
    .text("Generate and output a graph visualization of the workflow in .dot format" + System.lineSeparator)

  // Undocumented until fully-working
  //  cmd("v1upgrade")
  //    .action((_, c) => c.copy(command = Option(V1Upgrade)))
  //    .text("Automatically upgrade the WDL to version 1.0 and output the result." + System.lineSeparator)

  cmd("womgraph")
    .action((_, c) => c.copy(command = Option(WomGraph)))
    .text("(Advanced) Generate and output a graph visualization of Cromwell's internal Workflow Object Model structure for this workflow in .dot format" + System.lineSeparator)

}
